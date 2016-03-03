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

public class ListenerUnbound extends AbstractListenerState implements IListenerState  {

    private static final Logger logger = LoggerFactory.getLogger(ListenerUnbound.class);

    private static String STATE_NAME = "ListenerUnbound";
    
    public ListenerUnbound(final IListenerContext context) {
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
        logger.debug("VendorDialogueIndEvent event {} received in state {}", eventType, getStateName());
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
        logger.debug("processVendorGeneralIndEvent primitive {} received in state {}", primitive, getStateName());
        switch (primitive) {
            case TcBindIndEvent.PRIMITIVE_TC_BIND_IND:
                processTcBindIndEvent();
                break;
            case TcStateIndEvent.PRIMITIVE_TC_STATE_IND:
            case TcDialoguesLostIndEvent.PRIMITIVE_TC_DIALOGUES_LOST_IND:
            default:
                logger.error("VendorDialogueIndEvent event received in state {}", getStateName());
                throw new UnexpectedPrimitiveException(primitive);
        }
    }
    
    /**
     * Process a non-JAIN event:VendorGeneralIndEvent:TCBindIndEvent .
     */
    private void processTcBindIndEvent() {
        logger.debug("TcapBindIndEvent event received in state {}", getStateName());
        logger.debug("Changing state from {}", getStateName());
        context.setState(new ListenerBound(context));
    }
    
    protected String getStateName() {
    	return STATE_NAME;
    }
}
