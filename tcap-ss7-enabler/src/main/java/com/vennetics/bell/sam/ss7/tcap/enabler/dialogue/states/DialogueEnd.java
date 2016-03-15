package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.BadProtocolException;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;

public class DialogueEnd extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(DialogueEnd.class);

    private static String stateName = "DialogueEnd";

    public DialogueEnd(final IDialogueContext context, final IDialogue dialogue) {
        super(context, dialogue);
        logger.debug("Changing state to {}", getStateName());
    }

    public void activate() {
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
     *
     * @param event
     *
     * @exception SS7Exception
     */
    @Override
    public void processResultIndEvent(final ResultIndEvent event) throws SS7Exception {

        logger.debug("ResultIndEvent event received in state {}", getStateName());
        final JainTcapProvider provider = getContext().getProvider();
        final JainTcapStack stack = getContext().getStack();
        switch (stack.getProtocolVersion()) {
            case DialogueConstants.PROTOCOL_VERSION_ANSI_96:
                provider.releaseInvokeId(event.getLinkId(), event.getDialogueId());
                break;
            case DialogueConstants.PROTOCOL_VERSION_ITU_97:
                provider.releaseInvokeId(event.getInvokeId(), event.getDialogueId());
                break;
            default:
                throw new BadProtocolException("Wrong protocol version" + stack.getProtocolVersion());        }
        IDialogue dialogue = getContext().getDialogue(event.getDialogueId());
        if (dialogue == null) {
            logger.error("No dialogue found");
            return;
        }
        getContext().deactivateDialogue(dialogue);
    }

    public String getStateName() {
        return stateName;
    }
}
