package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.IDialogueState;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;

public interface IDialogue {
    
    int getDialogueId();

    JainTcapProvider getJainTcapProvider();

    IDialogueState getState();

    void setState(final IDialogueState state);

    boolean isEndReceived();

    void setEndReceived(final boolean val);
        
    void handleEvent(ComponentIndEvent event);

    void handleEvent(DialogueIndEvent event);
}
