package com.vennetics.bell.sam.ss7.tcap.ati.simulator.dialogue.states;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.ResultIndEvent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;

@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueEndTest {

    private static final int DIALOGUE_ID = 1111;
    private static final int LINK_ID = 33;

    @Mock
    private IDialogueContext mockDialogueContext;
    @Mock
    private JainTcapStack mockStack;
    @Mock
    private JainTcapProvider mockProvider;
    @Mock
    private TcapEventListener mockTcapListener;
    @Mock
    private IDialogue mockDialogue;
    @Mock
    private IDialogueManager mockDialogueMgr;

    private IDialogueState objectToTest;

    @Before
    public void setup() throws Exception {
        when(mockDialogueContext.getDialogueManager()).thenReturn(mockDialogueMgr);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest = new AtiSimDialogueEnd(mockDialogueContext, mockDialogue);
        verify(mockProvider).releaseDialogueId(DIALOGUE_ID);
        verify(mockDialogueMgr).deactivate(mockDialogue);
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfComponentIndEvent() throws Exception {
        final Operation operation = new Operation();
        objectToTest.handleEvent(new InvokeIndEvent(mockTcapListener, operation, true));
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionHandleIfResultIndEvent() throws Exception {
        final ResultIndEvent resultIndEvent = new ResultIndEvent(mockTcapListener,
                                                                 DIALOGUE_ID,
                                                                 true,
                                                                 true);
        resultIndEvent.setLinkId(LINK_ID);
        resultIndEvent.setDialogueId(DIALOGUE_ID);
        objectToTest.handleEvent(resultIndEvent);
    }
    
    @Ignore
    @Test
    public void shouldTerminate() {
      when(mockDialogueContext.getDialogueManager()).thenReturn(mockDialogueMgr);
      when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
      when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
      objectToTest.terminate();
      verify(mockProvider).releaseDialogueId(DIALOGUE_ID);
      verify(mockDialogueMgr).deactivate(mockDialogue);
    }
}
