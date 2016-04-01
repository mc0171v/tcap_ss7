package com.vennetics.bell.sam.ss7.tcap.ati.simulator.dialogue.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;

public class AtiSimDialogueEnd extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AtiSimDialogueEnd.class);

    private static String stateName = "AtiDialogueEnd";

    public AtiSimDialogueEnd(final IDialogueContext context, final IDialogue dialogue) {
        super(context, dialogue);
        logger.debug("Changing state to {}", getStateName());
        terminate();
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

    @Override
    public void processEndIndEvent(final EndIndEvent event) {
        logger.debug("Expected EndIndEvent received.");
    }

    @Override
    public String getStateName() {
        return stateName;
    }
}
