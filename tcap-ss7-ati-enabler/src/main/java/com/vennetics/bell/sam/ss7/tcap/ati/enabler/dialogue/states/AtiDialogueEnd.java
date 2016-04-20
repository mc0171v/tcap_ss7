package com.vennetics.bell.sam.ss7.tcap.ati.enabler.dialogue.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;

public class AtiDialogueEnd extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AtiDialogueEnd.class);

    private static String stateName = "AtiDialogueEnd";

    public AtiDialogueEnd(final IDialogue dialogue) {
        super(dialogue);
        logger.debug("Changing state to {}", getStateName());
        terminate();
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState#handleEvent(jain.protocol.ss7.tcap.ComponentIndEvent)
     */
    @Override
    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("ComponentIndEvent event received in state {}", getStateName());
        processComponentIndEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState#handleEvent(jain.protocol.ss7.tcap.DialogueIndEvent)
     */
    @Override
    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("DialogueIndEvent event received in state {}", getStateName());
        processDialogueIndEvent(event);
    }


    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState#processEndIndEvent(jain.protocol.ss7.tcap.dialogue.EndIndEvent)
     */
    @Override
    public void processEndIndEvent(final EndIndEvent event) {
        logger.debug("Expected EndIndEvent received.");
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState#getStateName()
     */
    @Override
    public String getStateName() {
        return stateName;
    }
}
