package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import java.util.concurrent.CountDownLatch;

import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;

public interface IDialogue {
    
    /***
     * Get this dialogues dialogueId
     * @return dialogueId
     */
    int getDialogueId();

    /***
     * Get the provider associated with this dialogue
     * @return {@link JainTcapProvider}
     */
    JainTcapProvider getJainTcapProvider();

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
     * Set the {@link IDialogueRequestBuilder} on the dialogue
     * @param dialogueRequestBuilder
     *     the dialogue request builder
     */
    void setDialogueRequestBuilder(IDialogueRequestBuilder dialogueRequestBuilder);

    /***
     * Set the {@link IComponentRequestBuilder} on the dialogue
     * @param componentRequestBuilder
     *     the component request builder
     */
    void setComponentRequestBuilder(IComponentRequestBuilder componentRequestBuilder);

    /***
     * Get the dialogue request builder associated with this dialogue
     * @return {@link IDialogueRequestBuilder}
     */
    IDialogueRequestBuilder getDialogueRequestBuilder();

    /***
     * Get the component request builder associated with this dialogue
     * @return {@link IComponentRequestBuilder}
     */
    IComponentRequestBuilder getComponentRequestBuilder();
    
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
