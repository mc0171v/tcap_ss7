package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.TcapUserAddress;

public interface IDialogueContext {
	
	int getSsn();
	
	IDialogueManager getDialogueManager();
	
    TcapUserAddress getDestAddr();

    TcapUserAddress getOrigAddr();
    
    JainTcapProvider getProvider();
    
    void startDialogue();
    
    JainTcapStack getStack();
}
