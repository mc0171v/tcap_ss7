package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.requests;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

public abstract class AbstractDialogueRequestBuilder implements IDialogueRequestBuilder {
    public BeginReqEvent createBeginReq(final IDialogueContext context, final int dialogueId) {

        final BeginReqEvent beginReq = new BeginReqEvent(context.getTcapEventListener(),
                                                         dialogueId,
                                                         context.getOrigAddr(),
                                                         context.getDestAddr());
        beginReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        beginReq.setOriginatingAddress(context.getOrigAddr());
        beginReq.setDestinationAddress(context.getDestAddr());
        beginReq.setDialoguePortion(new DialoguePortion());
        return beginReq;
    }
    
    public ContinueReqEvent createContinueReq(final IDialogueContext context, final int dialogueId) {

        ContinueReqEvent continueReq = new ContinueReqEvent(context.getTcapEventListener(), 0);
        continueReq.setDialogueId(dialogueId);
        continueReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        continueReq.setDialoguePortion(new DialoguePortion());
        return continueReq;
    }
    
    public EndReqEvent createEndReq(final IDialogueContext context, final int dialogueId) {

        EndReqEvent endReq = new EndReqEvent(context.getTcapEventListener(), 0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        endReq.setDialoguePortion(new DialoguePortion());
        return endReq;
    }
}
