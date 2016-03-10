package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;

public interface IDialogueState {

    void handleEvent(ComponentIndEvent event);

    void handleEvent(DialogueIndEvent event);
    
    void activate();
    
    String getStateName();
    
    void setContext(final IDialogueContext context);
    
    void setDialogue(final IDialogue dialogue);
    
}
