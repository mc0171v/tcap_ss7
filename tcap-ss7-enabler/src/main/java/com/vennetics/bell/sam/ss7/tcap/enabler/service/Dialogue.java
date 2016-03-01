package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.DialogueStarted;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.IDialogueState;

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

public class Dialogue implements IDialogue {

    private static final Logger logger = LoggerFactory.getLogger(Dialogue.class);


    
    private final IDialogueContext context;
    private int dialogueId;
    private final JainTcapProvider provider;
    private boolean isEndReceived = false;
    private IDialogueState state;

    /**
     * Dialogue constructor.
     * @param context
     * @param provider
     */
    public Dialogue(final IDialogueContext context,
                    final JainTcapProvider provider) {

        this.context = context;
        this.provider = provider;
        logger.debug("Started new Dialogue");
        this.state = new DialogueStarted(context);
    }
    


    public ContinueReqEvent createContinueReq(final java.lang.Object source, final int dialogueId) {

        final ContinueReqEvent endReq = new ContinueReqEvent(source, 0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        endReq.setDialoguePortion(new DialoguePortion());
        return endReq;
    }

    public static EndReqEvent createEndReq(final java.lang.Object source, final int dialogueId) {

        final EndReqEvent endReq = new EndReqEvent(source, 0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        endReq.setTermination(DialogueConstants.TC_BASIC_END);
        endReq.setDialoguePortion(new DialoguePortion());

        return endReq;
    }
    
    public int getDialogueId() {
        return dialogueId;
    }

    public JainTcapProvider getJainTcapProvider() {
        return provider;
    }

    public IDialogueState getState() {
        return state;
    }

    public void setState(final IDialogueState state) {
        this.state = state;
    }

    public void handleEvent(ComponentIndEvent event) {
    	state.handleEvent(event);
    }

    public void handleEvent(DialogueIndEvent event) {
    	state.handleEvent(event);
    }

    public boolean isEndReceived() {
        return isEndReceived;
    }

    public void setEndReceived(final boolean val) {
        isEndReceived = val;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getName() + "{ dialogueId: " + dialogueId + " SSN: " + context.getSsn() + "]";
    }

}
