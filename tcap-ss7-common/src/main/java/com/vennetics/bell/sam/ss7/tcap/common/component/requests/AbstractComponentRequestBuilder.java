package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public abstract class AbstractComponentRequestBuilder implements IComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentRequestBuilder.class);
    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request) {
        return createInvokeReq(source, invokeId, request, false);
    }
    
    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request,
                                          final boolean withParams) {
        logger.error("Unexpected createInvokeReq");
        return null;
    }
    
    public ResultReqEvent createResultReq(final IDialogueContext context,
                                   final int dialogueId,
                                   final int invokeId) {
        logger.error("Unexpected createResultReq");
        return null;
    }
}
