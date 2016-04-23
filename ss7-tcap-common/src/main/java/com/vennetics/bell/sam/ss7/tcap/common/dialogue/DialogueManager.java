package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ericsson.einss7.jtcap.TcDialoguesLostIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;


public class DialogueManager implements IDialogueManager {

    private final Map<Integer, IDialogue> dialogueMap;

    public DialogueManager() {
        dialogueMap = new ConcurrentHashMap<Integer, IDialogue>();
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager#activate(com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue)
     */
    @Override
    public void activate(final IDialogue dialogue) {
        if (null != dialogueMap.get(dialogue.getDialogueId())) {
            throw new Ss7ServiceException("Dialogue already exists " + dialogue.getDialogueId());

        }
        dialogueMap.put(dialogue.getDialogueId(), dialogue);
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager#clearAllDialogs()
     */
    @Override
    public void clearAllDialogs() {
        // call clear on each Dialogue?
        dialogueMap.clear();
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager#deactivate(com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue)
     */
    @Override
    public void deactivate(final IDialogue dialogue) {
        IDialogue old = dialogueMap.remove(dialogue.getDialogueId());
        if (old == null) {
            throw new Ss7ServiceException("Could not deactivate dialogue " + dialogue.getDialogueId());
        }
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager#cleanUpLostDialogues(com.ericsson.einss7.jtcap.TcDialoguesLostIndEvent)
     */
    @Override
    public void cleanUpLostDialogues(final TcDialoguesLostIndEvent event) {
        final Iterator<IDialogue> iter = dialogueMap.values().iterator();
        IDialogue dialogue;
        while (iter.hasNext()) {
            dialogue = iter.next();
            if (event.isDialogueIdLost(dialogue.getDialogueId())) {
                deactivate(dialogue);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager#isDialogueLeft()
     */
    @Override
    public boolean isDialogueLeft() {
        return !dialogueMap.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager#lookUpDialogue(java.lang.Integer)
     */
    @Override
    public IDialogue lookUpDialogue(final Integer dialogueId) {
        return dialogueMap.get(dialogueId);
    }
}
