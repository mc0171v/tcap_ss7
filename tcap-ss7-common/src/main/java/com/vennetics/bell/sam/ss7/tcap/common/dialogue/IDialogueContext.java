package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import java.util.concurrent.CountDownLatch;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.TcapUserAddress;

/***
 * The dialogue context for the {@link SamTcapEventListener}
 *
 */
public interface IDialogueContext {

    /***
     * Get the SSN associated with this dialogue context
     * @return The SSN
     */
    int getSsn();

    /***
     * Get the destination address associated with this dialogue context
     * @return the {@link TcapUserAddress}
     */
    TcapUserAddress getDestAddr();

    /***
     * Get the originating address associated with this dialogue context
     * @return the {@link TcapUserAddress}
     */
    TcapUserAddress getOrigAddr();

    /***
     * Get the TCAP {@link JainTcapProvider} associated with this dialogue context
     * @return {@link JainTcapProvider}
     */
    JainTcapProvider getProvider();

    /***
     * Get the TCAP {@link JainTcapStack} associated with this dialogue context
     * @return {@link JainTcapStack}
     */
    JainTcapStack getStack();

    /***
     * Get the TCAP {@link TcapEventListener} associated with this dialogue context
     * @return TcapEventListener
     */
    TcapEventListener getTcapEventListener();
    
    /***
     * Get the dialogue request builder associated with this context
     * 
     * @return {@link IDialogueRequestBuilder}
     */
    IDialogueRequestBuilder getDialogueRequestBuilder();

    /***
     * Get the component request builder associated with this context
     * 
     * @return {@link IComponentRequestBuilder}
     */
    IComponentRequestBuilder getComponentRequestBuilder();
    
    /***
     * Get the dialogue manager associated with this dialogue context
     * @return {@link IDialogueManager}
     */
    IDialogueManager getDialogueManager();

    /***
     * Get a dialogue associated with this dialogue context
     * @param dialogueId
     * @return {@link IDialogue}
     */
    IDialogue getDialogue(int dialogueId);

    /***
     * Deactivate a dialogue associated with this dialogue context
     * @param dialogueId
     */
    void deactivateDialogue(IDialogue dialogueId);

    /***
     * Start a dialogue associated with this dialogue context
     * @param dialogueId
     * @return {@link IDialogue}
     */
    IDialogue startDialogue(Object request, CountDownLatch latch);
    
    /***
     * Get the configuration properties associated with this dialogue context
     * @return {@link ISs7ConfigurationProperties}
     */
    ISs7ConfigurationProperties getConfigProperties();
}
