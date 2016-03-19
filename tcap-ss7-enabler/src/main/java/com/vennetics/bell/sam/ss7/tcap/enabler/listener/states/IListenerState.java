package com.vennetics.bell.sam.ss7.tcap.enabler.listener.states;

import com.ericsson.einss7.japi.VendorIndEvent;
import com.vennetics.bell.sam.ss7.tcap.enabler.listener.IListenerContext;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.TcapErrorEvent;

public interface IListenerState {

    void handleEvent(ComponentIndEvent event);

    void handleEvent(DialogueIndEvent event);

    void handleEvent(TcapErrorEvent event);

    void handleEvent(VendorIndEvent event);
    
    void setContext(IListenerContext listener);

}
