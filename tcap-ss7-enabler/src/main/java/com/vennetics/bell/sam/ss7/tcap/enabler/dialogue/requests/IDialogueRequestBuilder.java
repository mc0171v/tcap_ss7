package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.requests;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;

public interface IDialogueRequestBuilder {

    BeginReqEvent createBeginReq(final IDialogueContext context,
                                 final int dialogueId);
    
    ContinueReqEvent createContinueReq(IDialogueContext context, final int dialogueId);
}
