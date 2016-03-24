package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public interface IComponentRequestBuilder {

    InvokeReqEvent createInvokeReq(Object source,
                                   int invokeId,
                                   Object opData,
                                   ISs7ConfigurationProperties configProps,
                                   int dialogueId);

    InvokeReqEvent createInvokeReq(Object source,
                                   int invokeId,
                                   Object opData,
                                   boolean withparams,
                                   ISs7ConfigurationProperties configProps,
                                   int dialogueId);
    
    ResultReqEvent createResultReq(IDialogueContext context,
                                   int dialogueId,
                                   int invokeId);
}
