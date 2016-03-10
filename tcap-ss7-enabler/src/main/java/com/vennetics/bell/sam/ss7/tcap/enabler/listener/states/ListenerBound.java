package com.vennetics.bell.sam.ss7.tcap.enabler.listener.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.VendorComponentIndEvent;
import com.ericsson.einss7.japi.VendorDialogueIndEvent;
import com.ericsson.einss7.japi.VendorIndEvent;
import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.ericsson.einss7.jtcap.TcDialoguesLostIndEvent;
import com.ericsson.einss7.jtcap.TcStateIndEvent;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IListenerContext;

import jain.protocol.ss7.tcap.TcapUserAddress;

public class ListenerBound extends AbstractListenerState implements IListenerState {

    private static final Logger logger = LoggerFactory.getLogger(ListenerBound.class);

    private static String statename = "ListenerBound";

    public ListenerBound(final IListenerContext context) {
        super(context);
        logger.debug("Changing state to {}", getStateName());
    }

    /**
     * Receive a non-JAIN event (Ericsson Specific event).
     * 
     * @param event
     */
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
                logger.debug("VendorDialogueIndEvent event received in state ListenerBound");
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
            logger.debug("processTcStateIndEvent congestion: " + event.getUserStatus());
            // @todo handle congestion...

        } else {
            logger.debug("Changing state from {}", getStateName());
            getContext().setState(new ListenerReadyForTraffic(getContext()));
        }
    }

    /**
     * Check if the event indicates that the addr is ready for traffic.
     *
     * @param event
     * @param addr
     * @return True if ready.
     */
    private boolean isReadyForTraffic(final TcStateIndEvent event, final TcapUserAddress addr) {
        if (event.getUserStatus() == TcStateIndEvent.USER_UNAVAILABLE) {
            logger.debug("TcStateIndEvent.USER_UNAVAILABLE");
            return false;
        }

        // extract SPC and SSN from addr
        byte[] addrSpc = null;
        int addrSsn = -1;
        try {
            addrSpc = addr.getSignalingPointCode();
            addrSsn = addr.getSubSystemNumber();
        } catch (Exception ex) {
            logger.error("Failed to extract SPC and/or SSN");
            return false;
        }
        logger.debug("", addrSsn);

        // check that SPC in addr is the same as affected SPC
        byte[] affectedSpc = event.getAffectedSpc();
        logger.debug("Our SSN {} Affected SSN {}", addrSsn, event.getAffectedSsn());

        if (affectedSpc.length != addrSpc.length) {
            return false;
        }
        for (int i = 0; i < affectedSpc.length; i++) {
            logger.debug("{}: Our SPC {} Affected SPC {}", i, addrSpc[i], affectedSpc[i]);
        }
        for (int i = 0; i < affectedSpc.length; i++) {
            if (affectedSpc[i] != addrSpc[i]) {
                return false;
            }
        }

        // if SSN also matches then ready for traffic
        return event.getAffectedSsn() == addrSsn;
    }

    protected String getStateName() {
        return statename;
    }
}
