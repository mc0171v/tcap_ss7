package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.enabler.utils.EncodingHelper;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;

@Component
public class AtiComponentRequestBuilder extends AbstractComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AtiComponentRequestBuilder.class);
    private static final byte[] OPERATION_ATI = { 0x47 };
    private static final String GSMSCF_ADDRESS = "12344321"; //TODO config
    private static final long INVOKE_TIMEOUT = 10000; //TODO config
    
    private static final byte IMSI_TAG = Tools.getLoByteOf2(0x80);
    private static final byte SUBSCRIBER_IDENTITY_TAG = Tools.getLoByteOf2(0xA0);
    private static final byte MSISDN_TAG = Tools.getLoByteOf2(0x81);
    private static final byte GSMSCF_ADDRESS_TAG = Tools.getLoByteOf2(0x83);
    private static final byte REQUESTED_INFO_TAG = Tools.getLoByteOf2(0xA1);
    private static final byte LOCATION_INFORMATION_TAG = Tools.getLoByteOf2(0xA0);
    private static final byte SUBSCRIBER_STATE_TAG = Tools.getLoByteOf2(0xA1);
    

    AtiComponentRequestBuilder() {
        super();
        logger.debug("Constructed ATIComponentRequestBuilder");
    }


    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request,
                                          final boolean withparams) {

        final InvokeReqEvent invokeReq = new InvokeReqEvent(source, invokeId, null);
        invokeReq.setInvokeId(invokeId);
        invokeReq.setLastInvokeEvent(true);
        invokeReq.setTimeOut(INVOKE_TIMEOUT);
        invokeReq.setClassType(ComponentConstants.CLASS_3);
        final Operation op = new Operation();
        op.setOperationCode(OPERATION_ATI);
        op.setPrivateOperationData(OPERATION_ATI);
        op.setOperationType(Operation.OPERATIONTYPE_LOCAL);
        invokeReq.setOperation(op);
        OutboundATIMessage atiRequest = (OutboundATIMessage) request;
        if (withparams) {
            final byte[] params = createParameters(atiRequest);
            invokeReq.setParameters(new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, params));
        }
        return invokeReq;
    }

    public byte[] createParameters(final OutboundATIMessage request) {
        final ByteBuffer subscriberInfo = buildSubsciberIdentityElement(request);
        final ByteBuffer requestedInfo = buildRequestInfoElement(request);
        final ByteBuffer gsmScfAddress = EncodingHelper.buildIsdnAddressStringElement(GSMSCF_ADDRESS,
                                                                                      GSMSCF_ADDRESS_TAG);
        ByteBuffer bb = ByteBuffer.allocate(subscriberInfo.capacity()
                                            + requestedInfo.capacity()
                                            //+ gsmScfAddress.capacity() + 1 + asn1Length.length);
                                            + gsmScfAddress.capacity());

        bb.put(subscriberInfo.array()).put(requestedInfo.array()).put(gsmScfAddress.array());
        return bb.array();
        
    }
    
    private int getRequestInfoLenth(final OutboundATIMessage request) {
        int requestInfoLength = 0;
        requestInfoLength = request.isRequestInfoLocation() ? requestInfoLength + 2 : requestInfoLength;
        requestInfoLength = request.isRequestInfoSubscriberState() ? requestInfoLength + 2 : requestInfoLength;
        return requestInfoLength;
    }
    
   private ByteBuffer buildRequestInfoElement(final OutboundATIMessage request) {
       final byte tag = REQUESTED_INFO_TAG;
       final byte[] asn1Length = EncodingHelper.getAsn1Length(getRequestInfoLenth(request));
       ByteBuffer bb = ByteBuffer.allocate(getRequestInfoLenth(request) + asn1Length.length + 1);
       bb.put(tag).put(asn1Length);
       if (request.isRequestInfoLocation()) {
           bb.put(EncodingHelper.buildNullElement(LOCATION_INFORMATION_TAG).array());
       }
       if (request.isRequestInfoSubscriberState()) {
           bb.put(EncodingHelper.buildNullElement(SUBSCRIBER_STATE_TAG).array());
       }
       logger.debug(EncodingHelper.bytesToHex(bb.array()));
       return bb;
   }
   
   private ByteBuffer buildSubsciberIdentityElement(final OutboundATIMessage request) {
       final byte tag = SUBSCRIBER_IDENTITY_TAG;
       ByteBuffer subInfoBb;
       if (request.getImsi() != null) {
           subInfoBb = EncodingHelper.buildIsdnAddressStringElement(request.getImsi(), IMSI_TAG);
       } else {
           subInfoBb = EncodingHelper.buildIsdnAddressStringElement(request.getMsisdn(), MSISDN_TAG);
       }
       final byte[] asn1Length = EncodingHelper.getAsn1Length(subInfoBb.capacity());
       ByteBuffer bb = ByteBuffer.allocate(subInfoBb.capacity() + asn1Length.length + 1);
       bb.put(tag).put(asn1Length).put(subInfoBb.array());
       logger.debug(EncodingHelper.bytesToHex(bb.array()));
       return bb;
   }
   
}
