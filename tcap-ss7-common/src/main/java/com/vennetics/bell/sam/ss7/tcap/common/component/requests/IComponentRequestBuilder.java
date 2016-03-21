package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public interface IComponentRequestBuilder {

    InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                   final int invokeId,
                                   final Object opData);

    InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                   final int invokeId,
                                   final Object opData,
                                   final boolean withparams);
    
    ResultReqEvent createResultReq(final IDialogueContext context,
                                   final int dialogueId,
                                   final int invokeId);
}
