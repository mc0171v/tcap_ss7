package com.vennetics.bell.sam.ss7.tcap.ati.simulator.component.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.common.component.requests.AbstractComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

@Component
public class AtiComponentRequestBuilder extends AbstractComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AtiComponentRequestBuilder.class);
    
    public static final byte[] ATI  = { 0x47 };
    
    AtiComponentRequestBuilder() {
        super();
        logger.debug("Constructed ATIComponentResultBuilder");
    }

    public ResultReqEvent createResultReq(final IDialogueContext context,
                                           final int dialogueId,
                                           final int invokeId) {
        ResultReqEvent resReq = new ResultReqEvent(context, dialogueId, true);
        resReq.setInvokeId(invokeId);
        final Operation op = new Operation();
        op.setOperationCode(ATI);
        op.setOperationType(Operation.OPERATIONTYPE_LOCAL);
        resReq.setOperation(op);
        //final byte[] bytes = {0x30, 0x06, 0x30, 0x04, Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00 };
        final byte[] bytes = { 0x30, 0x12, 0x30, 0x10, Tools.getLoByteOf2(0xA0), 0x0E, 0x02, 0x02, 0x01, 0x03, Tools.getLoByteOf2(0x80),
                0x08, 0x10, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x01 };
        Parameters params = new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, bytes);
        resReq.setParameters(params);
        return resReq;
    }
   
}
