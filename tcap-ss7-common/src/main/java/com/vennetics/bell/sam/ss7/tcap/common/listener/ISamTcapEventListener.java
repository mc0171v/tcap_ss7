package com.vennetics.bell.sam.ss7.tcap.common.listener;

import java.util.concurrent.CountDownLatch;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

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
    
    ISs7ConfigurationProperties getConfigProperties();
    
    void setProvider(JainTcapProvider provider);
    
    void setStack(JainTcapStack stack);
    
    void setDialogueManager(IDialogueManager dialogueManager);
}
