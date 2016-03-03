package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.DialogueStart;
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

    /**
     * Dialogue constructor.
     * @param context
     * @param provider
     */
    public Dialogue(final IDialogueContext context,
                    final JainTcapProvider provider) {

        this.context = context;
        this.provider = provider;
        logger.debug("Started new Dialogue");
        this.state = new DialogueStart(context, this);
    }
    
    public int getDialogueId() {
        return dialogueId;
    }

    public void setDialogueId(final int dialogueId) {
		this.dialogueId = dialogueId;
		context.getDialogueManager().activate(this);
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

    public void handleEvent(ComponentIndEvent event) {
    	logger.debug("Handing off event to state {}", state.getStateName());
    	state.handleEvent(event);
    }

    public void handleEvent(DialogueIndEvent event) {
    	logger.debug("Handing off event to state {}", state.getStateName());
    	state.handleEvent(event);
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getName() + "{ dialogueId: " + dialogueId + " SSN: " + context.getSsn() + "]";
    }
    
    public void activate() {
    	state.activate();
    }

    public String getStateName() {
    	return state.getStateName();
    }
}
