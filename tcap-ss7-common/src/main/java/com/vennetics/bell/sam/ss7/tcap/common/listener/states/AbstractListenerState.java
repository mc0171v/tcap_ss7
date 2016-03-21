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

    public AbstractListenerState(final IListenerContext context) {
        this.context = context;
    }
    
    public AbstractListenerState() {
    }

    @Override
    public void handleEvent(final ComponentIndEvent event) {
        processComponentIndEvent(event);
    }

    @Override
    public void handleEvent(final DialogueIndEvent event) {
        processDialogueIndEvent(event);
    }

    @Override
    public void handleEvent(final TcapErrorEvent event) {
        processTcapError(event);
    }

    @Override
    public void handleEvent(final VendorIndEvent event) {
        processVendorIndEvent(event);
    }

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
     */
    protected synchronized void processTcapError(final TcapErrorEvent tcapError) {
        handleTcapError(tcapError);
    }

    /**
     * 
     * @param tcapError
     */
    protected void handleTcapError(final TcapErrorEvent tcapError) {

        // cleanup before calling initAppl again
        context.cleanup();

        // must clear old dialogues in case there are started dialogues
        // in the old provider
        context.clearAllDialogs();

        // try to get things going again
        context.initialise(false);
    }

    /**
     * Dialogue Events dispatching.
     *
     * @param event
     */
    protected void processDialogueIndEvent(final DialogueIndEvent event) {

        logger.debug("DialogueIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    /**
     * Receive a non-JAIN event (Ericsson Specific event).
     * 
     * @param event
     */
    protected void processVendorIndEvent(final VendorIndEvent event) {
        logger.debug("VendorIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }
    
    public IListenerContext getContext() {
        return context;
    }
    
    public void setContext(final IListenerContext context) {
        this.context = context;
    }
}
