package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.utils.IResultListener;

public interface IBellSamTcapEventListener extends TcapEventListener, IListenerContext,
                                           IDialogueContext {

    boolean isReady();
    
    boolean isBound();
    
    void initialise(final boolean bool);
    
    IDialogue startDialogue(Object request, IResultListener resultListener);
    
    void cleanup();
    
    void setConfiguration();
}
