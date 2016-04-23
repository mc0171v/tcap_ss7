package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import com.ericsson.einss7.jtcap.TcDialoguesLostIndEvent;

public interface IDialogueManager {

    /**
     * Activate a Dialogue.
     * 
     * @param dialogue
     *            a dialogue to activate
     */
    void activate(IDialogue dialogue);

    /**
     * Removes all dialogues.
     */
    void clearAllDialogs();

    /**
     * Deactivate a Dialogue.
     * 
     * @param dialogue
     *            the dialogue to deactivate.
     */
    void deactivate(IDialogue dialogue);

    /**
     * Clean lost dialogues
     * 
     * @param event
     */
    void cleanUpLostDialogues(TcDialoguesLostIndEvent event);

    /**
     * Verify if there are alive dialogues.
     * 
     * @return true if dialogue map is not empty.
     */
    boolean isDialogueLeft();

    /**
     * Get a Dialogue object by dialogueID.
     * 
     * @param dialogueId
     *            a dialogueID
     * @return the Dialogue object
     */
    IDialogue lookUpDialogue(Integer dialogueId);

}
