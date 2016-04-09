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
     * @param provider
     */
    public Dialogue(final IDialogueContext context,
                    final Object request) {
        this.request = request;
        this.context = context;
        logger.debug("Started new Dialogue");
    }

    @Override
    public int getDialogueId() {
        return dialogueId;
    }

    @Override
    public void setDialogueId(final int dialogueId) {
        this.dialogueId = dialogueId;
        context.getDialogueManager().activate(this);
        logger.debug("Activated Dialogue with ID {}", dialogueId);
    }

    @Override
    public JainTcapProvider getJainTcapProvider() {
        return context.getProvider();
    }

    @Override
    public IDialogueState getState() {
        return state;
    }

    @Override
    public void setState(final IDialogueState state) {
        this.state = state;
    }

    @Override
    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("Handing off event to state {}", state.getStateName());
        state.handleEvent(event);
    }

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

    @Override
    public void activate() {
        state.activate();
    }

    @Override
    public String getStateName() {
        return state.getStateName();
    }
    
    @Override
    public void setDialogueRequestBuilder(final IDialogueRequestBuilder dialogueRequestBuilder) {
        this.dialogueRequestBuilder = dialogueRequestBuilder;
    }

    @Override
    public void setComponentRequestBuilder(final IComponentRequestBuilder componentRequestBuilder) {
        this.componentRequestBuilder = componentRequestBuilder;
    }
    
    @Override
    public IDialogueRequestBuilder getDialogueRequestBuilder() {
        return dialogueRequestBuilder;
    }

    @Override
    public IComponentRequestBuilder getComponentRequestBuilder() {
        return componentRequestBuilder;
    }

    @Override
    public Object getRequest() {
        return request;
    }

    public void setRequest(final Object request) {
        this.request = request;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void setResult(final Object result) {
        this.result = result;
        latch.countDown(); //Inform command that result is now available.
    }
    
    @Override
    public void setError(final Ss7ServiceException error) {
        this.result = request;
        ((IError) result).setError(error);
        latch.countDown(); //Inform command that result is now available.
    }
    
    @Override
    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
    public void setLatch(final CountDownLatch latch) {
        this.latch = latch;
    }
}
