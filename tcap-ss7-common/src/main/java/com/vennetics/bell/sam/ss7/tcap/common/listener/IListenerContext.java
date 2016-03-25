package com.vennetics.bell.sam.ss7.tcap.common.listener;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.TcapUserAddress;

public interface IListenerContext {
    void cleanup();

    void clearAllDialogs();

    void initialise(boolean isFirst);

    void setState(IListenerState state);
    
    TcapUserAddress getDestinationAddress();
    
    IDialogue getDialogue(final int dialogueId);
    
    ISs7ConfigurationProperties getConfigProperties();
}
