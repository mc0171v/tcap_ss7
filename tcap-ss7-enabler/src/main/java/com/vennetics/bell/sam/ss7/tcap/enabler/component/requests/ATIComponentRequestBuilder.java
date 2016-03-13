package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;

@Component
public class ATIComponentRequestBuilder implements IComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ATIComponentRequestBuilder.class);
    private static final byte[] OPERATION_ATI = { 0x47 };
    private static final String GSMSCF_ADDRESS = "12344321";

    ATIComponentRequestBuilder() {
        logger.debug("Constructed ATIComponentRequestBuilder");
    }
    
    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request) {
        return createInvokeReq(source, invokeId, request, false);
    }


    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request,
                                          final boolean withparams) {

        final InvokeReqEvent invokeReq = new InvokeReqEvent(source, invokeId, null);
        invokeReq.setInvokeId(invokeId);
        invokeReq.setLastInvokeEvent(true);
        invokeReq.setTimeOut(100000);
        invokeReq.setClassType(ComponentConstants.CLASS_3);
        final Operation op = new Operation(); // FIX: Operation class, CODE to
                                              // TYPE in
        // constants
        op.setOperationCode(OPERATION_ATI);
        op.setPrivateOperationData(OPERATION_ATI);
        op.setOperationType(Operation.OPERATIONTYPE_LOCAL); // FIX: code vs.
                                                             // type
                                                             // missunderstanding
        invokeReq.setOperation(op);
        OutboundATIMessage atiRequest = (OutboundATIMessage) request;
        if (withparams) {
            final byte[] params = createParameters(atiRequest);
            invokeReq.setParameters(new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, params));
        }
        return invokeReq;
    }

    public byte[] createParameters(final OutboundATIMessage request) {
        final ByteBuffer subscriberInfo = buildSubsciberInfoElement(request);
        final ByteBuffer requestedInfo = buildRequestInfoElement(request);
        final ByteBuffer gsmScfAddress = buildGsmScfAddressElement(GSMSCF_ADDRESS);
        final byte tag = 0x30;
        final byte[] asn1Length = getAsn1Length(subscriberInfo.capacity()
                                              + requestedInfo.capacity()
                                              + gsmScfAddress.capacity());
        ByteBuffer bb = ByteBuffer.allocate(subscriberInfo.capacity()
                                            + requestedInfo.capacity()
                                            + gsmScfAddress.capacity() + 1 + asn1Length.length);
        bb.put(tag).put(asn1Length).put(subscriberInfo.array()).put(requestedInfo.array()).put(gsmScfAddress.array());
        return bb.array();
        
    }
    
    private int getRequestInfoLenth(final OutboundATIMessage request) {
        int requestInfoLength = 0;
        requestInfoLength = request.isRequestInfoLocation() ? requestInfoLength + 2 : requestInfoLength;
        requestInfoLength = request.isRequestInfoSubscriberState() ? requestInfoLength + 2 : requestInfoLength;
        return requestInfoLength;
    }
    
    protected byte[] getAsn1Length(final int paramLength) {
        byte[] byteArray;
        if (paramLength <= 0x7F) {
            byteArray = new byte[1];
            byteArray[0] = Tools.getLoByteOf2(paramLength);
        } else if (paramLength <= 0xFF) {
            byteArray = new byte[2];
            byteArray[0]  = Tools.getLoByteOf2(0x81); // length tag
            byteArray[1]  = Tools.getLoByteOf2(paramLength);
        } else {
            byteArray = new byte[3];
            byteArray[0]  = Tools.getLoByteOf2(0x81); // length tag
            byteArray[1]  = Tools.getLoByteOf2(paramLength);
            byteArray[2] = Tools.getHiByteOf2(paramLength);
        }
        return byteArray;
    }
    
    public static byte[] hexStringToByteArray(final String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    
   private ByteBuffer buildImsiElement(final OutboundATIMessage request) {
       final byte[] imsi = hexStringToByteArray(request.getImsi());
       final byte tag = Tools.getLoByteOf2(0x80);
       final byte[] asn1Length = getAsn1Length(imsi.length);
       ByteBuffer bb = ByteBuffer.allocate(imsi.length + asn1Length.length + 1);
       bb.put(tag).put(asn1Length).put(imsi);
       logger.debug(bytesToHex(bb.array()));
       return bb;
   }
   
   private ByteBuffer buildMsisdnElement(final OutboundATIMessage request) {
       final byte[] msisdn = hexStringToByteArray(request.getMsisdn());
       final byte tag = Tools.getLoByteOf2(0x81);
       final byte[] asn1Length = getAsn1Length(msisdn.length);
       ByteBuffer bb = ByteBuffer.allocate(msisdn.length + asn1Length.length + 1);
       bb.put(tag).put(asn1Length).put(msisdn);
       logger.debug(bytesToHex(bb.array()));
       return bb;
   }
   
   private ByteBuffer buildGsmScfAddressElement(final String gsmScfAddress) {
       final byte[] address = hexStringToByteArray(gsmScfAddress);
       final byte tag = Tools.getLoByteOf2(0x83);
       final byte[] asn1Length = getAsn1Length(address.length);
       ByteBuffer bb = ByteBuffer.allocate(address.length + asn1Length.length + 1);
       bb.put(tag).put(asn1Length).put(address);
       logger.debug(bytesToHex(bb.array()));
       return bb;
   }
   
   private ByteBuffer buildRequestInfoSubscriberStateElement() {
       final byte tag = Tools.getLoByteOf2(0x81);
       final byte[] asn1Length = getAsn1Length(0);
       ByteBuffer bb = ByteBuffer.allocate(asn1Length.length + 1);
       bb.put(tag).put(asn1Length);
       logger.debug(bytesToHex(bb.array()));
       return bb;
   }
   
   private ByteBuffer buildRequestInfoSubscriberLocationElement() {
       final byte tag = Tools.getLoByteOf2(0x80);
       final byte[] asn1Length = getAsn1Length(0);
       ByteBuffer bb = ByteBuffer.allocate(asn1Length.length + 1);
       bb.put(tag).put(asn1Length);
       logger.debug(bytesToHex(bb.array()));
       return bb;
   }

   private ByteBuffer buildRequestInfoElement(final OutboundATIMessage request) {
       final byte tag = Tools.getLoByteOf2(0xA1);
       final byte[] asn1Length = getAsn1Length(getRequestInfoLenth(request));
       ByteBuffer bb = ByteBuffer.allocate(getRequestInfoLenth(request) + asn1Length.length + 1);
       bb.put(tag).put(asn1Length);
       if (request.isRequestInfoLocation()) {
           bb.put(buildRequestInfoSubscriberLocationElement().array());
       }
       if (request.isRequestInfoSubscriberState()) {
           bb.put(buildRequestInfoSubscriberStateElement().array());
       }
       logger.debug(bytesToHex(bb.array()));
       return bb;
   }
   
   private ByteBuffer buildSubsciberInfoElement(final OutboundATIMessage request) {
       final byte tag = Tools.getLoByteOf2(0xA0);
       ByteBuffer subInfoBb;
       if (request.getImsi() != null) {
           subInfoBb = buildImsiElement(request);
       } else {
           subInfoBb = buildMsisdnElement(request);
       }
       final byte[] asn1Length = getAsn1Length(subInfoBb.capacity());
       ByteBuffer bb = ByteBuffer.allocate(subInfoBb.capacity() + asn1Length.length + 1);
       bb.put(tag).put(asn1Length).put(subInfoBb.array());
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
   
}
