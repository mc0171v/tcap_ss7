package com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

public interface IDialogueRequestBuilder {

    /***
     * Create a TCAP Begin/Query to start a dialogue
     * @param context
     *     the dialogue context
     * @param dialogueId
     *     the dialogueId
     * @return
     *     the {@link BeginReqEvent}
     */
    BeginReqEvent createBeginReq(final IDialogueContext context,
                                 final int dialogueId);
    
    /***
     * Create a TCAP Continue/Conversation to continue an existing  dialogue
     * @param context
     *     the dialogue context
     * @param dialogueId
     *     the dialogueId
     * @return
     *     the {@link ContinueReqEvent}
     */
    ContinueReqEvent createContinueReq(IDialogueContext context,
                                       final int dialogueId);
    
    /***
     * Create a TCAP End/Response to end the dialogue
     * @param context
     *     the dialogue context
     * @param dialogueId
     *     the dialogueId
     * @return
     *     the {@link EndReqEvent}
     */
    EndReqEvent createEndReq(IDialogueContext context,
                             final int dialogueId);
}
