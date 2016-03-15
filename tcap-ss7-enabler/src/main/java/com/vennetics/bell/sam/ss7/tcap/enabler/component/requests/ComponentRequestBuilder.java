package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;

@Component
public class ComponentRequestBuilder extends AbstractComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ComponentRequestBuilder.class);
    private static final int PARAM_LEN = Integer.parseInt(System.getProperty("PARAM_LEN", "30"));
    private static final byte[] OPERATION_CONTINUE_DIALOGUE = { 0x01 };


    ComponentRequestBuilder() {
        logger.debug("Constructed ComponentRequestBuilder");
    }
    
    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request) {
        return createInvokeReq(source, invokeId, request, false);
    }


    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object opData,
                                          final boolean withparams) {

        return createInvokeReq(source, invokeId, opData, withparams, PARAM_LEN, false);
    }


    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request,
                                          final boolean withparams,
                                          final int paramLen,
                                          final boolean isRawParam) {

        final InvokeReqEvent invokeReq = new InvokeReqEvent(source, invokeId, null);
        invokeReq.setInvokeId(invokeId);
        invokeReq.setLastInvokeEvent(true);
        invokeReq.setTimeOut(100000);
        invokeReq.setClassType(ComponentConstants.CLASS_3);
        final Operation op = new Operation(); // FIX: Operation class, CODE to
                                              // TYPE in
        // constants
        op.setOperationCode(OPERATION_CONTINUE_DIALOGUE);
        op.setPrivateOperationData(OPERATION_CONTINUE_DIALOGUE);
        op.setOperationFamily(Operation.OPERATIONFAMILY_PROCEDURAL);
        op.setOperationSpecifier(Operation.CHARGINGSPECIFIER_BILLCALL);
        op.setOperationType(Operation.OPERATIONTYPE_GLOBAL); // FIX: code vs.
                                                             // type
                                                             // missunderstanding
        invokeReq.setOperation(op);
        if (withparams) {
            final byte[] params = createParameters(paramLen, isRawParam);

            invokeReq.setParameters(new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, params));
        }
        return invokeReq;
    }

    protected byte[] createParameters(final int paramLength, final boolean isRaw) {

        final byte[] params = new byte[paramLength];
        int pos = 0;
        if (isRaw) {
            params[pos++] = Tools.getLoByteOf2(0x30); // sequence tag
            pos = setLength(paramLength, params, pos); // total length
        }
        params[pos++] = Tools.getLoByteOf2(0x04); // OCTETSTRING id (ASN.1)
        pos = setLength(paramLength - pos - 1, params, pos); // length of
                                                             // OCTETSTRING
        for (int i = pos; i < paramLength - pos; i++) {
            // fill up with dummy data
            params[i] = Tools.getLoByteOf2(i - pos);
        }
        return params;
    }

    protected int setLength(final int paramLength, final byte[] params, final int pos) {
        int currentPos = pos;
        if (paramLength <= 0x7F) {
            params[currentPos++] = Tools.getLoByteOf2(paramLength);
        } else if (paramLength <= 0xFF) {
            params[currentPos++] = Tools.getLoByteOf2(0x81); // length tag
            params[currentPos++] = Tools.getLoByteOf2(paramLength);
        } else {
            params[currentPos++] = Tools.getLoByteOf2(0x82); // length tag
            params[currentPos++] = Tools.getLoByteOf2(paramLength);
            params[currentPos++] = Tools.getHiByteOf2(paramLength);
        }
        return currentPos;
    }
}
