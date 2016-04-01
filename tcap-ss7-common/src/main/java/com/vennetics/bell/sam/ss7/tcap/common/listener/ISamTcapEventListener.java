package com.vennetics.bell.sam.ss7.tcap.common.listener;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;

public interface ISamTcapEventListener extends TcapEventListener, IListenerContext,
                                           IDialogueContext {

    boolean isReady();
        
    boolean isBound();
                
    void setConfiguration();
        
    void setProvider(JainTcapProvider provider);
    
    void setStack(JainTcapStack stack);
    
    void setDialogueManager(IDialogueManager dialogueManager);
}
