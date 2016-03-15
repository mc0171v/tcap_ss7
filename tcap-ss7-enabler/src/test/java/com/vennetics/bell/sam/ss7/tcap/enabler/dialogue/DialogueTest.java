package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;

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
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.DialogueStart;


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
    @Mock
    private IDialogueManager mockDialogueManager;
    @Mock
    private CountDownLatch mockLatch;

    private IDialogue objectToTest;

    @Before
    public void setup() {
        objectToTest = new Dialogue(mockListener, mockProvider, getRequestObject());
        objectToTest.setState(mockState);
        objectToTest.setLatch(mockLatch);
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
    
    @Test
    public void shouldActivateDialogueWhenDialogueIdSet() throws Exception {
        when(mockListener.getDialogueManager()).thenReturn(mockDialogueManager);
        objectToTest.setDialogueId(DIALOGUE_ID);
        verify(mockDialogueManager).activate(objectToTest);
    }
 
    @Test
    public void shouldCountDownLatchWhenResultSet() throws Exception {
        objectToTest.setResult(getRequestObject());
        verify(mockLatch).countDown();
    }
    
    private Object getRequestObject() {
        return new Object();
    }

}
