package com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

public abstract class AbstractDialogueRequestBuilder implements IDialogueRequestBuilder {
    
    private String builderType;
    
    protected AbstractDialogueRequestBuilder(final String builderName) {
        this.builderType = builderName;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder#createBeginReq(com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext, int)
     */
    @Override
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
    
    /*
     * (non-Javadoc)
     * @see
     * com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder#createContinueReq(com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext, int)
     */
    @Override
    public ContinueReqEvent createContinueReq(final IDialogueContext context, final int dialogueId) {

        ContinueReqEvent continueReq = new ContinueReqEvent(context.getTcapEventListener(), 0);
        continueReq.setDialogueId(dialogueId);
        continueReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        continueReq.setDialoguePortion(new DialoguePortion());
        return continueReq;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder#createEndReq(com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext, int)
     */
    @Override
    public EndReqEvent createEndReq(final IDialogueContext context, final int dialogueId) {

        EndReqEvent endReq = new EndReqEvent(context.getTcapEventListener(), 0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        endReq.setDialoguePortion(new DialoguePortion());
        return endReq;
    }
     
    public String getBuilderType() {
        return builderType;
    }
}
