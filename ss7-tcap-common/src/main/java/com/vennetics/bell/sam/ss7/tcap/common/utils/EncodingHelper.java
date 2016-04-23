package com.vennetics.bell.sam.ss7.tcap.common.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;

import ericsson.ein.ss7.commonparts.util.Tools;

public final class EncodingHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(EncodingHelper.class);
    
    private static final int TAG_LENGTH = 1;
    private static final int NULL_LENGTH = 0;
    private static final int MAX_SINGLE_OCTET_LENGTH = 0x7F;
    private static final int MAX_DOUBLE_OCTET_LENGTH = 0xFF;
    private static final int LONG_LENGTH_TAG = 0x80;
    private static final int SINGLE_LENGTH_OCTET_TAG = 0x81;
    private static final int DOUBLE_LENGTH_OCTET_TAG = 0x82;
    private static final String SPECIAL_BCD_CHARS = "*#abcf*#ABCF";
    private static final String FILLER = "f";
    private static final String LENGTH_DEBUG = "Length: {}";
    
    public static final int SEQUENCE_TAG = 0x30;
    public static final int INTEGER_TAG = 0x02;
    public static final int ENUMERATED_TAG = 0x0A;
    
    private EncodingHelper() {
        
    }
    
    /***
     * Get the ASN1 definite representation for a given length
     * @param paramLength
     *    the length of the parameter
     * @return
     *     a byte array containing the ASN1 definite representation
     */
    public static byte[] getAsn1Length(final int paramLength) {
        byte[] byteArray;
        if (paramLength <= MAX_SINGLE_OCTET_LENGTH) {
            byteArray = new byte[1];
            byteArray[0] = Tools.getLoByteOf2(paramLength);
        } else if (paramLength <= MAX_DOUBLE_OCTET_LENGTH) {
            byteArray = new byte[2];
            byteArray[0]  = Tools.getLoByteOf2(SINGLE_LENGTH_OCTET_TAG);
            byteArray[1]  = Tools.getLoByteOf2(paramLength);
        } else {
            byteArray = new byte[3];
            byteArray[0]  = Tools.getLoByteOf2(DOUBLE_LENGTH_OCTET_TAG);
            byteArray[1]  = Tools.getHiByteOf2(paramLength);
            byteArray[2] = Tools.getLoByteOf2(paramLength);
        }
        return byteArray;
    }

    /***
     * Get the ASN1 ISDN-Address representation of a given address string
     * @param addressString
     *     the supplied address string
     * @param tag
     *     the ASN1 tag for the address string as a byte
     * @return
     *     a {@link ByteBuffer} containing the ASN1 TLV for the address
     */
    public static ByteBuffer buildIsdnAddressStringElement(final String addressString, final byte tag) {
        final byte[] element = hexTeleStringToByteArray(addressString);
        final byte[] asn1Length = getAsn1Length(element.length);
        ByteBuffer bb = ByteBuffer.allocate(element.length + asn1Length.length + TAG_LENGTH);
        bb.put(tag).put(asn1Length).put(element);
        logger.debug(bytesToHex(bb.array()));
        return bb;
    }
    
    /***
     * Get the ASN1 representation for a tagged null element
     * @param tag
     *     the ASN1 tag for the null element
     * @return
     *     a {@link ByteBuffer} containing the ASN1 TLV for the null element
     */
    public static ByteBuffer buildNullElement(final byte tag) {
        final byte[] asn1Length = getAsn1Length(NULL_LENGTH);
        ByteBuffer bb = ByteBuffer.allocate(asn1Length.length + TAG_LENGTH);
        bb.put(tag).put(asn1Length);
        logger.debug(bytesToHex(bb.array()));
        return bb;
    }

    /***
     * Utility method to print out a array of bytes as hex
     * @param bytes
     *     the array of bytes to be displayed
     * @return
     *     the hex string
     */
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
    
    /***
     * Utility method to print out a array of bytes as hex
     * @param bytes
     *     the array of bytes to be displayed
     * @return
     *     the hex string
     */
    public static String bytesToHex(final Byte[] bytes) {
        return bytesToHex(byteToByte(bytes));
    }
   
    /***
     * Utility method to print out a byte as hex
     * @param aByte
     *     the byte to be displayed
     * @return
     *     the hex string
     */
    public static String bytesToHex(final byte aByte) {
        final byte[] bytes = new byte[1];
        bytes[0] = aByte;
        return bytesToHex(bytes);
    }
    
    /***
     * Converts a hex Tele-String to a byte array
     * @param hexString
     *     the hex string to be converted
     * @return
     *     the byte array representation of the hex string
     */
    public static byte[] hexTeleStringToByteArray(final String hexString) {
         if (hexString != null && !hexString.isEmpty()) {
             int len = hexString.length();
             String newString = hexString;
             if (len % 2 != 0) {
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
     
     private static int convertToTBCDCharacter(final char character) {
         final int val = Character.digit(character, 10);
         if (val >= 0) {
             return val;
         }
         if (SPECIAL_BCD_CHARS.indexOf(character) < 0) {
             throw new Ss7ServiceException("Could not convert " + Character.toString(character));
         }
         return 10 + (SPECIAL_BCD_CHARS.indexOf(character) % 6);
     }
    
     /***
      * Return the Outer TLV's for a given ASN1 byte array
      * @param bytes
      *     the ASN1 byte array
      * @return
      *     the list of {@link TagLengthValue}
      */
     public static List<TagLengthValue> getTlvs(final byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        final List<TagLengthValue> tlvs = new ArrayList<TagLengthValue>();
        while (bb.hasRemaining()) {
            final TagLengthValue tlv = new TagLengthValue();
            final byte tag = bb.get(); //Assuming single byte tags
            tlv.setTag(tag);
            logger.debug("Tag: {}", EncodingHelper.bytesToHex(tag));
            int valueLength = 0;
            int lengthLength;
            byte lengthOrLengthTag = bb.get();
            if ((lengthOrLengthTag & LONG_LENGTH_TAG) > 0x0) {
                lengthLength = bytes[1] & 0x7F;
                if (lengthLength == 1) {
                    byte octet1 = bb.get();
                    valueLength = (int) octet1 & 0xFF;
                    final byte[] length = { lengthOrLengthTag, octet1 };
                    tlv.setLength(length);
                    logger.debug(LENGTH_DEBUG, EncodingHelper.bytesToHex(length));
                } else if (lengthLength == 2) {
                    final byte octet1 = bb.get();
                    final byte octet2 = bb.get();
                    final byte[] length = { lengthOrLengthTag, octet1, octet2 };
                    valueLength = ((octet1 & 0xFF) << 8) + (octet2 & 0xFF);
                    tlv.setLength(length);
                    logger.debug(LENGTH_DEBUG, EncodingHelper.bytesToHex(length));
                } else { //Don't think we'll be dealing with anything longer
                    logger.error("Parameters too long");
                }
            } else {
                valueLength = lengthOrLengthTag;
                final byte[] length = { lengthOrLengthTag };
                tlv.setLength(length);
                logger.debug(LENGTH_DEBUG, EncodingHelper.bytesToHex(length));
            }
            final byte[] value = new byte[valueLength];
            bb.get(value, 0, valueLength);
            tlv.setValue(value);
            logger.debug("Value: {}", EncodingHelper.bytesToHex(value));
            tlvs.add(tlv);
        }
        return tlvs;
    }
     
     public static byte[] byteToByte(Byte[] bytes) {
         final byte[] byteArray = new byte[bytes.length];
         int i = 0;
         for (Byte aByte : bytes) {
             byteArray[i++] = aByte;
         }
         return byteArray;
     }
}
