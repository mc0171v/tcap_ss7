package com.vennetics.bell.sam.ss7.tcap.common.dialogue.states;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;

public interface IDialogueState {

    /***
     * Handle a {@link ComponentIndEvent} in this state
     * @param event
     *    the {@link ComponentIndEvent} to be handled
     */
    void handleEvent(ComponentIndEvent event);

    /***
     * Handle a {@link DialogueIndEvent} in this state
     * @param event
     *    the {@link DialogueIndEvent} to be handled
     */
    void handleEvent(DialogueIndEvent event);
    
    /***
     * Activate a dialogue in this state
     */
    void activate();
    
    
    /***
     * Terminate a dialogue in this state
     */
    void terminate();
   
    /***
     * Get the state nmae
     * @return
     *     state name
     */
    String getStateName();
    
    /***
     * Set the {@link IDialogueContext}
     * @param context
     *     the dialogue context
     */
    void setContext(final IDialogueContext context);
    
    /***
     * Set the dialogue {@link IDialogue}
     * @param dialogue
     *     the dialogue
     */
    void setDialogue(final IDialogue dialogue);

}
