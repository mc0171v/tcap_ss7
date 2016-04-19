package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.error.IError;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;

public class Dialogue implements IDialogue {
    private static final Logger logger = LoggerFactory.getLogger(Dialogue.class);

    private final IDialogueContext context;
    private int dialogueId;
    
    private IDialogueState state;

    private Object request;
    
    private Object result;
    private CountDownLatch latch;

    private IDialogueRequestBuilder dialogueRequestBuilder;
    private IComponentRequestBuilder componentRequestBuilder;

    /**
     * Dialogue constructor.
     * 
     * @param context
     *     the dialogue context {@link IDialogueContext}
     * @param request
     *     the request object associated with this dialogue
     */
    public Dialogue(final IDialogueContext context,
                    final Object request) {
        this.request = request;
        this.context = context;
        logger.debug("Started new Dialogue");
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getDialogueId()
     */
    @Override
    public int getDialogueId() {
        return dialogueId;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setDialogueId(int)
     */
    @Override
    public void setDialogueId(final int dialogueId) {
        this.dialogueId = dialogueId;
        context.getDialogueManager().activate(this);
        logger.debug("Activated Dialogue with ID {}", dialogueId);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getJainTcapProvider()
     */
    @Override
    public JainTcapProvider getJainTcapProvider() {
        return context.getProvider();
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getState()
     */
    @Override
    public IDialogueState getState() {
        return state;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setState(com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState)
     */
    @Override
    public void setState(final IDialogueState state) {
        this.state = state;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#handleEvent(jain.protocol.ss7.tcap.ComponentIndEvent)
     */
    @Override
    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("Handing off event to state {}", state.getStateName());
        state.handleEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#handleEvent(jain.protocol.ss7.tcap.DialogueIndEvent)
     */
    @Override
    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("Handing off event to state {}", state.getStateName());
        state.handleEvent(event);
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getName() + "{ dialogueId: " + dialogueId + " SSN: "
                        + context.getSsn() + "]";
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#activate()
     */
    @Override
    public void activate() {
        state.activate();
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getStateName()
     */
    @Override
    public String getStateName() {
        return state.getStateName();
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setDialogueRequestBuilder(com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder)
     */
    @Override
    public void setDialogueRequestBuilder(final IDialogueRequestBuilder dialogueRequestBuilder) {
        this.dialogueRequestBuilder = dialogueRequestBuilder;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setComponentRequestBuilder(com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder)
     */
    @Override
    public void setComponentRequestBuilder(final IComponentRequestBuilder componentRequestBuilder) {
        this.componentRequestBuilder = componentRequestBuilder;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getDialogueRequestBuilder()
     */
    @Override
    public IDialogueRequestBuilder getDialogueRequestBuilder() {
        return dialogueRequestBuilder;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getComponentRequestBuilder()
     */
    @Override
    public IComponentRequestBuilder getComponentRequestBuilder() {
        return componentRequestBuilder;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getRequest()
     */
    @Override
    public Object getRequest() {
        return request;
    }

    /*
     * 
     */
    public void setRequest(final Object request) {
        this.request = request;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getResult()
     */
    @Override
    public Object getResult() {
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setResult(java.lang.Object)
     */
    @Override
    public void setResult(final Object result) {
        this.result = result;
        latch.countDown(); //Inform command that result is now available.
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setError(com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException)
     */
    @Override
    public void setError(final Ss7ServiceException error) {
        this.result = request;
        ((IError) result).setError(error);
        latch.countDown(); //Inform command that result is now available.
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#getLatch()
     */
    @Override
    public CountDownLatch getLatch() {
        return latch;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue#setLatch(java.util.concurrent.CountDownLatch)
     */
    @Override
    public void setLatch(final CountDownLatch latch) {
        this.latch = latch;
    }
}
