package com.vennetics.bell.sam.ss7.tcap.ati.enabler.dialogue.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;

public class AtiDialogueEnd extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AtiDialogueEnd.class);

    private static String stateName = "AtiDialogueEnd";

    public AtiDialogueEnd(final IDialogueContext context, final IDialogue dialogue) {
        super(context, dialogue);
        logger.debug("Changing state to {}", getStateName());
    }

    public void activate() {
    }
    
    public void terminate() {
    }

    @Override
    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("ComponentIndEvent event received in state {}", getStateName());
        processComponentIndEvent(event);
    }

    @Override
    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("DialogueIndEvent event received in state {}", getStateName());
        processDialogueIndEvent(event);
    }

    /**
     * Dialogue event.
     */
    public void processEndIndEvent(final EndIndEvent event) throws SS7Exception {
        logger.debug("Expected EndIndEvent received.");
    }

    public String getStateName() {
        return stateName;
    }
}
