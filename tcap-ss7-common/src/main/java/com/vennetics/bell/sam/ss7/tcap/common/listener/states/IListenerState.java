package com.vennetics.bell.sam.ss7.tcap.common.listener.states;

import com.ericsson.einss7.japi.VendorIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.listener.IListenerContext;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.TcapErrorEvent;

public interface IListenerState {

    /***
     * Handle a component indication event in this state
     * @param event
     *     The {@link ComponentIndEvent}
     */
    void handleEvent(ComponentIndEvent event);

    /***
     * Handle a dialogue indication event in this state
     * @param event
     *     The {@link DialogueIndEvent}
     */
    void handleEvent(DialogueIndEvent event);

    /***
     * Handle a TCAP error event in this state
     * @param event
     *     The {@link TcapErrorEvent}
     */
    void handleEvent(TcapErrorEvent event);

    /***
     * Handle a vendor indication event in this state
     * @param event
     *     The {@link VendorIndEvent}
     */
    void handleEvent(VendorIndEvent event);

    /***
     * Set the listener context
     * @param listener
     *     the {@link IListenerContext}
     */
    void setContext(IListenerContext listener);

}
