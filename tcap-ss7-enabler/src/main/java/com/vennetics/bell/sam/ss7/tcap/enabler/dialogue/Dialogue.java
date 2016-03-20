package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.enabler.common.IError;
import com.vennetics.bell.sam.ss7.tcap.enabler.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.IDialogueState;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;

public class Dialogue implements IDialogue {
    private static final Logger logger = LoggerFactory.getLogger(Dialogue.class);

    private final IDialogueContext context;
    private int dialogueId;

    private final JainTcapProvider provider;
    
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
     * @param provider
     */
    public Dialogue(final IDialogueContext context,
                    final JainTcapProvider provider,
                    final Object request) {
        this.request = request;
        this.context = context;
        this.provider = provider;
        logger.debug("Started new Dialogue");
    }

    public int getDialogueId() {
        return dialogueId;
    }

    public void setDialogueId(final int dialogueId) {
        this.dialogueId = dialogueId;
        context.getDialogueManager().activate(this);
        logger.debug("Activated Dialogue with ID {}", dialogueId);
    }

    public JainTcapProvider getJainTcapProvider() {
        return provider;
    }

    public IDialogueState getState() {
        return state;
    }

    public void setState(final IDialogueState state) {
        this.state = state;
    }

    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("Handing off event to state {}", state.getStateName());
        state.handleEvent(event);
    }

    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("Handing off event to state {}", state.getStateName());
        state.handleEvent(event);
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getName() + "{ dialogueId: " + dialogueId + " SSN: "
                        + context.getSsn() + "]";
    }

    public void activate() {
        state.activate();
    }

    public String getStateName() {
        return state.getStateName();
    }
    
    public void setDialogueRequestBuilder(final IDialogueRequestBuilder dialogueRequestBuilder) {
        this.dialogueRequestBuilder = dialogueRequestBuilder;
    }

    public void setComponentRequestBuilder(final IComponentRequestBuilder componentRequestBuilder) {
        this.componentRequestBuilder = componentRequestBuilder;
    }
    
    public IDialogueRequestBuilder getDialogueRequestBuilder() {
        return dialogueRequestBuilder;
    }

    public IComponentRequestBuilder getComponentRequestBuilder() {
        return componentRequestBuilder;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(final Object request) {
        this.request = request;
    }


    public Object getResult() {
        return result;
    }

    public void setResult(final Object result) {
        this.result = result;
        latch.countDown(); //Inform command that result is now available.
    }
    
    public void setError(final String error) {
        this.result = request;
        ((IError) result).setError(error);
        latch.countDown(); //Inform command that result is now available.
    }
    
    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(final CountDownLatch latch) {
        this.latch = latch;
    }
}
