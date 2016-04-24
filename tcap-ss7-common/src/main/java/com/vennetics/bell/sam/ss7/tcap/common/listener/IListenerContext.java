package com.vennetics.bell.sam.ss7.tcap.common.listener;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.TcapUserAddress;

public interface IListenerContext {
    
    /***
     * Cleanup listener and provider
     */
    void cleanup();

    /***
     * Clear all dialogues
     */
    void clearAllDialogs();

    /***
     * Initialise Jain stack, provider and listener
     */
    void initialise();

    /***
     * Set the listener state on this listener context
     * @param state
     */
    void setState(IListenerState state);
 
    /***
     * Get the destination address associated with this listener context
     * @return destination address {@link TcapUserAddress}
     */
    TcapUserAddress getDestinationAddress();

    /***
     * Retrieve an existing dialogue associated with this listener context
     * @param dialogueId
     *     the dialogueId
     * @return
     *     the dialogue {@link IDialogue}
     */
    IDialogue getDialogue(int dialogueId);
    
    /***
     * Get the configuration properties associated with this listener context
     * @return
     *     the configuration properties {@link ISs7ConfigurationProperties}
     */
    ISs7ConfigurationProperties getConfigProperties();
    
    /***
     * Join a dialogue initiated in another TCAP
     * @param dialogueId
     *     the dialogueId of the dialogue to join
     * @param type
     *     the type name of the dialogue
     * @return
     *     the dialogue {@link IDialogue}
     */
    IDialogue joinDialogue(int dialogueId, String type);
}
