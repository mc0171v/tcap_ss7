package com.vennetics.bell.sam.ss7.tcap.common.listener.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.VendorComponentIndEvent;
import com.ericsson.einss7.japi.VendorDialogueIndEvent;
import com.ericsson.einss7.japi.VendorIndEvent;
import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.ericsson.einss7.jtcap.TcDialoguesLostIndEvent;
import com.ericsson.einss7.jtcap.TcStateIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.IListenerContext;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.ParameterNotSetException;
import jain.protocol.ss7.tcap.TcapConstants;
import jain.protocol.ss7.tcap.TcapUserAddress;

public class ListenerReadyForTraffic extends AbstractListenerState implements IListenerState {

    private static final Logger logger = LoggerFactory.getLogger(ListenerReadyForTraffic.class);

    private static String stateName = "ListenerReadyForTraffic";
    
    private static final String NOEXTRACT = "Could not extract dialogue Id: {}";

    public ListenerReadyForTraffic(final IListenerContext context) {
        super(context);
        logger.debug("Changing state to {}", getStateName());
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.AbstractListenerState#handleEvent(jain.protocol.ss7.tcap.ComponentIndEvent)
     */
    @Override
    public void handleEvent(final ComponentIndEvent event) {
        processComponentIndEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.AbstractListenerState#handleEvent(jain.protocol.ss7.tcap.DialogueIndEvent)
     */
    @Override
    public void handleEvent(final DialogueIndEvent event) {
        processDialogueIndEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.AbstractListenerState#handleEvent(com.ericsson.einss7.japi.VendorIndEvent)
     */
    @Override
    public void handleEvent(final VendorIndEvent event) {
        processVendorIndEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.AbstractListenerState#processComponentIndEvent(jain.protocol.ss7.tcap.ComponentIndEvent)
     */
    @Override
    protected void processComponentIndEvent(final ComponentIndEvent event) {
        logger.debug("ComponentIndEvent event received in state {}", getStateName());
        IDialogue dialogue = getDialogue(event);
        if (null != dialogue) {
            dialogue.handleEvent(event);
        }
    }

    private IDialogue getDialogue(final ComponentIndEvent event) {
        int dialogueId = 0;
        try {
            dialogueId = event.getDialogueId();
            logger.debug("Retrieved dialogId {}", dialogueId);
        } catch (ParameterNotSetException ex) {
            logger.error(NOEXTRACT, ex);
            return null;
        }
        IDialogue dialogue = getContext().getDialogue(dialogueId);
        logger.debug("Retrieved dialogue {} for dialogId {}", dialogue, dialogueId);
        return dialogue;
    }

    private IDialogue getDialogue(final DialogueIndEvent event) {
        int dialogueId = 0;
        try {
            dialogueId = event.getDialogueId();
            logger.debug("Retrieved dialogId {}", dialogueId);
        } catch (ParameterNotSetException ex) {
            logger.error(NOEXTRACT, ex);
            return null;
        }
        IDialogue dialogue = getContext().getDialogue(dialogueId);
        logger.debug("Retrieved dialogue {} for dialogId {}", dialogue, dialogueId);
        return dialogue;
    }

    /**
     * Dialogue Events dispatching.
     *
     * @param event
     */
    @Override
    protected void processDialogueIndEvent(final DialogueIndEvent event) {
        logger.debug("DialogueIndEvent event received in state {}", getStateName());
        IDialogue dialogue = getDialogue(event);
        if (null != dialogue) {
            dialogue.handleEvent(event);
            return;
        }
        if (event.getPrimitiveType() == TcapConstants.PRIMITIVE_BEGIN) {
            int dialogueId = 0;
            try {
                dialogueId = event.getDialogueId();
                getContext().joinDialogue(dialogueId);
                logger.debug("Retrieved dialogId {} and created new dialogue", dialogueId);
            } catch (ParameterNotSetException ex) {
                logger.error(NOEXTRACT, ex);
            }
        }
    }

    /**
     * Receive a non-JAIN event (Ericsson Specific event).
     * 
     * @param event
     */
    @Override
    protected void processVendorIndEvent(final VendorIndEvent event) {
        final int eventType = event.getVendorEventType();
        logger.debug("VendorIndEvent event {} received in state {}", eventType, getStateName());
        switch (eventType) {
            case VendorIndEvent.VENDOR_EVENT_GENERAL_IND:
                processVendorGeneralIndEvent(event);
                break;
            case VendorComponentIndEvent.VENDOR_EVENT_COMPONENT_IND:
            case VendorDialogueIndEvent.VENDOR_EVENT_DIALOGUE_IND:
            default:
                final int primitive = event.getPrimitiveType();
                throw new UnexpectedPrimitiveException(primitive);
        }
    }

    /**
     * Process a non-JAIN event: VendorGeneralIndEvent.
     * 
     * @param event
     *            The indication event that is going to be processed.
     */
    private void processVendorGeneralIndEvent(final VendorIndEvent event) {
        final int primitive = event.getPrimitiveType();
        logger.debug("processVendorGeneralIndEvent primitive {} received in state {}",
                     primitive,
                     getStateName());
        switch (primitive) {
            case TcStateIndEvent.PRIMITIVE_TC_STATE_IND:
                processTcStateIndEvent((TcStateIndEvent) event);
                break;
            case TcBindIndEvent.PRIMITIVE_TC_BIND_IND:
            case TcDialoguesLostIndEvent.PRIMITIVE_TC_DIALOGUES_LOST_IND:
            default:
                throw new UnexpectedPrimitiveException(primitive);
        }
    }

    /**
     * Process a non-JAIN event:VendorGeneralIndEvent:TCStateIndEvent .
     * 
     * @param event
     *            The indication event that is going to be processed.
     */
    private void processTcStateIndEvent(final TcStateIndEvent event) {
        if (!isReadyForTraffic(event, getContext().getDestinationAddress())) {
            logger.debug("Changing state from {}", getStateName());
            getContext().setState(new ListenerBound(getContext()));
        } else {
            logger.debug("processTcStateIndEvent HD in service: " + event.getUserStatus());

        }
    }

    /**
     * Check if the event indicates that the addr is still ready for traffic.
     *
     * @param event
     * @param addr
     * @return True if ready.
     */
    private boolean isReadyForTraffic(final TcStateIndEvent event, final TcapUserAddress addr) {
        if (!getContext().getConfigProperties().isWaitForReady()
         || !checkSpcMatches(event, addr)
         || !checkSsnMatchesAndUnavailable(event, addr)) {
            return true;
        }
        return false;
    }

    private static boolean checkSpcMatches(final TcStateIndEvent event, final TcapUserAddress addr) {
        // extract SPC and SSN from addr
        byte[] addrSpc = null;
        try {
            addrSpc = addr.getSignalingPointCode();
        } catch (Exception ex) {
            logger.error("Failed to extract SPC {}", ex);
            return false;
        }

        // check that SPC in addr is the same as affected SPC
        byte[] affectedSpc = event.getAffectedSpc();

        if (affectedSpc.length != addrSpc.length) {
            logger.debug("TcStateIndEvent SPC's don't match");
            return false;
        }

        for (int i = 0; i < affectedSpc.length; i++) {
            if (affectedSpc[i] != addrSpc[i]) {
                logger.debug("TcStateIndEvent SPC's don't match");
                return false;
            }
        }
        
        return true;
    }

    private static boolean checkSsnMatchesAndUnavailable(final TcStateIndEvent event, final TcapUserAddress addr) {
        int addrSsn = -1;
        try {
            addrSsn = addr.getSubSystemNumber();
        } catch (Exception ex) {
            logger.error("Failed to extract SSN: {}", ex);
            return false;
        }

        if (event.getUserStatus() == TcStateIndEvent.USER_UNAVAILABLE
                        && event.getAffectedSsn() == addrSsn) {
            logger.debug("TcStateIndEvent.USER_UNAVAILABLE");
            return true;
        }
        
        return false;
    }
    protected String getStateName() {
        return stateName;
    }
}
