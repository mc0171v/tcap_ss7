package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue;

import com.vennetics.bell.sam.ss7.tcap.enabler.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.IDialogueState;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;

public interface IDialogue {
    
    int getDialogueId();

    JainTcapProvider getJainTcapProvider();

    IDialogueState getState();

    void setState(IDialogueState state);
    
    void setDialogueId(int dialogueId);
        
    void handleEvent(ComponentIndEvent event);

    void handleEvent(DialogueIndEvent event);
    
    void activate();
    
    String getStateName();
    
    Object getRequest();

    void setDialogueRequestBuilder(IDialogueRequestBuilder dialogueRequestBuilder);

    void setComponentRequestBuilder(IComponentRequestBuilder componentRequestBuilder);

    IDialogueRequestBuilder getDialogueRequestBuilder();

    IComponentRequestBuilder getComponentRequestBuilder();
}
