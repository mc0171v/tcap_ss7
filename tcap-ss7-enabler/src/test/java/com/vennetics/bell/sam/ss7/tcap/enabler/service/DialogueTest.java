package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import static org.mockito.Mockito.verify;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.dialogue.BeginIndEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.Dialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.DialogueStart;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;


@RunWith(MockitoJUnitRunner.class)
public class DialogueTest {

    private static final int DIALOGUE_ID = 1111;
    private static final short SSN = 8;

    @Mock
    private IDialogueContext mockListener;
    @Mock
    private TcapEventListener mockTcapListener;
    @Mock
    private JainTcapProvider mockProvider;
    @Mock
    private DialogueStart mockState;

    private IDialogue objectToTest;

    @Before
    public void setup() {
        objectToTest = new Dialogue(mockListener, mockProvider, getRequestObject());
        objectToTest.setState(mockState);
    }

    @Test
    public void shouldHandleDialogueIndEvent() throws Exception {
        byte[] byteString = "address".getBytes("UTF-8");
        TcapUserAddress sourceAddress = new TcapUserAddress(byteString, SSN);
        TcapUserAddress destAddress = new TcapUserAddress(byteString, SSN);
        BeginIndEvent event = new BeginIndEvent(mockTcapListener,
                                                DIALOGUE_ID,
                                                sourceAddress,
                                                destAddress,
                                                true);
        objectToTest.handleEvent(event);
        verify(mockState).handleEvent(event);
    }

    @Test
    public void shouldHandleComponentIndEvent() throws Exception {
        final Operation operation = new Operation();
        InvokeIndEvent event = new InvokeIndEvent(mockTcapListener, operation, true);
        event.setDialogueId(DIALOGUE_ID);
        objectToTest.handleEvent(event);
        verify(mockState).handleEvent(event);
    }
       
    private OutboundATIMessage getRequestObject() {
        return new OutboundATIMessage();
    }

}
