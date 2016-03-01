package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import com.vennetics.bell.sam.ss7.tcap.enabler.listener.states.IListenerState;

import jain.protocol.ss7.tcap.TcapUserAddress;

public interface IListenerContext {
    void cleanup();

    void clearAllDialogs();

    void initialise(boolean isFirst);

    void setState(IListenerState state);
    
    TcapUserAddress getDestinationAddress();
    
    IDialogue getDialogue(final int dialogueId);
}
