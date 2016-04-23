package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import java.util.concurrent.CountDownLatch;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;

public interface IDialogue {
    
    /***
     * Get this dialogues dialogueId
     * @return dialogueId
     */
    int getDialogueId();

    /***
     * Get this dialogues context
     * @return {@link IDialogueContext}
     */
    IDialogueContext getContext();
    
    /***
     * get the current state of this dialogue
     * @return {@link IDialogueState}
     */
    IDialogueState getState();

    /***
     * Set the current state of this dialogue
     * @param state
     *     the {@link IDialogueState} of this dialogue
     */
    void setState(IDialogueState state);
    
    /***
     * Set the dialogueId on this dialogue
     * @param dialogueId
     */
    void setDialogueId(int dialogueId);
        
    /***
     * Handle a {@link ComponentIndEvent} in this dialogue
     * @param event
     *     the event to handle
     */
    void handleEvent(ComponentIndEvent event);

    /***
     * Handle a {@link DialogueIndEvent} in this dialogue
     * @param event
     *     the event to handle
     */
    void handleEvent(DialogueIndEvent event);
    
    /***
     * Activate this dialogue
     */
    void activate();
    
    /***
     * Get the stateName of this dialogue
     * @return the name of the dialogues current state
     */
    String getStateName();
    
    /***
     * Set the result object on this dialogue
     * @param result
     *     the result of this dialogue
     */
    void setResult(Object result);
    
    /***
     * Set an {@link Ss7ServiceException} on thie dialogue
     * @param error
     *     the error result of this dialogue
     */
    void setError(Ss7ServiceException error);
    
    /***
     * Get the request object set on this dialogue
     * @return request
     *     the request to be executed on this dialogue
     */
    Object getRequest();
    
    /***
     * Get the result object set on this dialogue
     * @return request
     *     the result of this dialogue
     */
    Object getResult();

    /***
     * Set a {@link CountDownLatch} on this dialogue
     * @param cDl
     */
    void setLatch(CountDownLatch cDl);
    
    /***
     * Get the {@link CountDownLatch} associated with this dialogue
     * @return
     */
    CountDownLatch getLatch();
}
