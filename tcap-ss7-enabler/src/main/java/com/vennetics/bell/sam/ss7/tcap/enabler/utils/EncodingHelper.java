package com.vennetics.bell.sam.ss7.tcap.enabler.utils;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.enabler.exception.BadTBCDException;

import ericsson.ein.ss7.commonparts.util.Tools;

public final class EncodingHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(EncodingHelper.class);
    
    private static final int TAG_LENGTH = 1;
    private static final int NULL_LENGTH = 0;
    private static final int MAX_SINGLE_OCTET_LENGTH = 0x7F;
    private static final int MAX_DOUBLE_OCTET_LENGTH = 0xFF;
    private static final int LENGTH_TAG = 0x81;
    private static final String SPECIAL_BCD_CHARS = "*#abcf*#ABCF";
    private static final String FILLER = "f";
    
    private EncodingHelper() {
        
    }
    
    public static byte[] getAsn1Length(final int paramLength) {
        byte[] byteArray;
        if (paramLength <= MAX_SINGLE_OCTET_LENGTH) {
            byteArray = new byte[1];
            byteArray[0] = Tools.getLoByteOf2(paramLength);
        } else if (paramLength <= MAX_DOUBLE_OCTET_LENGTH) {
            byteArray = new byte[2];
            byteArray[0]  = Tools.getLoByteOf2(LENGTH_TAG);
            byteArray[1]  = Tools.getLoByteOf2(paramLength);
        } else {
            byteArray = new byte[3];
            byteArray[0]  = Tools.getLoByteOf2(LENGTH_TAG);
            byteArray[1]  = Tools.getLoByteOf2(paramLength);
            byteArray[2] = Tools.getHiByteOf2(paramLength);
        }
        return byteArray;
    }

    public static ByteBuffer buildIsdnAddressStringElement(final String addressString, final byte tag) {
        final byte[] element = hexTeleStringToByteArray(addressString);
        final byte[] asn1Length = getAsn1Length(element.length);
        ByteBuffer bb = ByteBuffer.allocate(element.length + asn1Length.length + TAG_LENGTH);
        bb.put(tag).put(asn1Length).put(element);
        logger.debug(bytesToHex(bb.array()));
        return bb;
    }
    
    public static ByteBuffer buildNullElement(final byte tag) {
        final byte[] asn1Length = getAsn1Length(NULL_LENGTH);
        ByteBuffer bb = ByteBuffer.allocate(asn1Length.length + TAG_LENGTH);
        bb.put(tag).put(asn1Length);
        logger.debug(bytesToHex(bb.array()));
        return bb;
    }

    public static String bytesToHex(final byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static byte[] hexTeleStringToByteArray(final String hexString) {
         if (hexString != null && !hexString.isEmpty()) {
             int len = hexString.length();
             String newString = hexString;
             if ((len % 2) != 0) {
                 newString = newString + FILLER;
                 len = len + 1;
             }
             byte[] data = new byte[len / 2];
             for (int i = 0; i < len; i += 2) {
                 data[i / 2] = (byte) ((convertToTBCDCharacter(newString.charAt(i + 1)) << 4)
                                 + convertToTBCDCharacter(newString.charAt(i)));
             }
             return data;
         }
         return new byte[0];
     }
     
    public static int convertToTBCDCharacter(final char character) {
         final int val = Character.digit(character, 10);
         if (val >= 0 || val <= 9) {
             return val;
         }
         if (SPECIAL_BCD_CHARS.indexOf(character) < 0) {
             throw new BadTBCDException("Could not convert " + Character.toString(character));
         }
         return 10 + (SPECIAL_BCD_CHARS.indexOf(character) % 6);
     }
}
