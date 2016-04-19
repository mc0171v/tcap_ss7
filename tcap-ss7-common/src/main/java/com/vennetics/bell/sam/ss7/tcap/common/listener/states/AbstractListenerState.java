package com.vennetics.bell.sam.ss7.tcap.common.listener.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.VendorIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.IListenerContext;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.TcapErrorEvent;

public abstract class AbstractListenerState implements IListenerState {

    private static final Logger logger = LoggerFactory.getLogger(AbstractListenerState.class);

    private IListenerContext context;

    /***
     * Create a listener state
     * @param context
     *     the listener context {@link IListenerContext}
     */
    public AbstractListenerState(final IListenerContext context) {
        this.context = context;
    }
    
    public AbstractListenerState() {
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState#handleEvent(jain.protocol.ss7.tcap.ComponentIndEvent)
     */
    @Override
    public void handleEvent(final ComponentIndEvent event) {
        processComponentIndEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState#handleEvent(jain.protocol.ss7.tcap.DialogueIndEvent)
     */
    @Override
    public void handleEvent(final DialogueIndEvent event) {
        processDialogueIndEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState#handleEvent(jain.protocol.ss7.tcap.TcapErrorEvent)
     */
    @Override
    public void handleEvent(final TcapErrorEvent event) {
        processTcapError(event);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState#handleEvent(com.ericsson.einss7.japi.VendorIndEvent)
     */
    @Override
    public void handleEvent(final VendorIndEvent event) {
        processVendorIndEvent(event);
    }

    /***
     * Process a {@link ComponentIndEvent}
     * @param event
     *     the event to process
     */
    protected void processComponentIndEvent(final ComponentIndEvent event) {
        logger.debug("ComponentIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    /**
     * A fatal error has occurred in the API implementation.
     * Clean up and re-create all JTCAP objects.
     * 
     * @param tcapError
     *     the TCAP error event {@link TcapErrorEvent}
     */
    protected synchronized void processTcapError(final TcapErrorEvent tcapError) {
        handleTcapError(tcapError);
    }

    /**
     * Handle a TCAP error
     * @param tcapError
     *     the TCAP error event {@link TcapErrorEvent}
     */
    protected void handleTcapError(final TcapErrorEvent tcapError) {
        logger.error("Received TCAP Error cleaning up and reinitialising: {}", tcapError.getError().toString());
        
        // cleanup before calling initAppl again
        context.cleanup();

        // must clear old dialogues in case there are started dialogues
        // in the old provider
        context.clearAllDialogs();

        // try to get things going again
        context.initialise();
    }

    /**
     * Process a dialogue indication event event
     *
     * @param event
     *     the {@link DialogueIndEvent}
     */
    protected void processDialogueIndEvent(final DialogueIndEvent event) {

        logger.debug("DialogueIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    /**
     * Receive a non-JAIN vendor indication event (Ericsson Specific event).
     * 
     * @param event
     *     the {@link VendorIndEvent}
     */
    protected void processVendorIndEvent(final VendorIndEvent event) {
        logger.debug("VendorIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }
    
    /***
     * Get the listener context
     * @return
     *     {@link IListenerContext}
     */
    public IListenerContext getContext() {
        return context;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState#setContext(com.vennetics.bell.sam.ss7.tcap.common.listener.IListenerContext)
     */
    @Override
    public void setContext(final IListenerContext context) {
        this.context = context;
    }
}
