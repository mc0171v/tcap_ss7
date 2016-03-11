package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.utils.IResultListener;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.TcapUserAddress;

public interface IDialogueContext {

    int getSsn();

    IDialogueManager getDialogueManager();

    TcapUserAddress getDestAddr();

    TcapUserAddress getOrigAddr();

    JainTcapProvider getProvider();

    JainTcapStack getStack();

    TcapEventListener getTcapEventListener();

    IDialogue getDialogue(int dialogueId);

    void deactivateDialogue(IDialogue dialogueId);

    IDialogue startDialogue(Object request, IResultListener resultListener);
}
