package com.vennetics.bell.sam.ss7.tcap.common.listener;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;

public interface ISamTcapEventListener extends TcapEventListener, IListenerContext,
                                           IDialogueContext {

    /***
     * Indicates if the listener is ready to handle traffic
     * @return
     *     boolean indicating readiness of listener
     */
    boolean isReady();
       
    /***
     * Indicates if the listener is bound to a listener subsystem
     * @return
     *     boolean indicating if listener is bound
     */
    boolean isBound();
         
    /***
     * Apply the listener configuration
     */
    void setConfiguration();
      
    /***
     * Set the Jain TCAP provider
     * @param provider
     *     the jain TCAP provider to set
     */
    void setProvider(JainTcapProvider provider);
    
    /***
     * Set the Jain TCAP stack
     * @param stack
     * the jain TCAP stack to set
     */
    void setStack(JainTcapStack stack);
    
    /***
     * Set the dialogue manager
     * @param dialogueManager
     *     the dialogue manager to set
     */
    void setDialogueManager(IDialogueManager dialogueManager);
}
