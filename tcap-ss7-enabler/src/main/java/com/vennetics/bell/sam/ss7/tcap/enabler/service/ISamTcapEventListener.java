package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import java.util.concurrent.CountDownLatch;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueManager;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;

public interface ISamTcapEventListener extends TcapEventListener, IListenerContext,
                                           IDialogueContext {

    boolean isReady();
    
    boolean isBound();
    
    void initialise(final boolean bool);
    
    IDialogue startDialogue(Object request, CountDownLatch cDl);
    
    void cleanup();
    
    void setConfiguration();
    
    void setProvider(JainTcapProvider provider);
    
    void setStack(JainTcapStack stack);
    
    void setDialogueManager(IDialogueManager dialogueManager);
}
