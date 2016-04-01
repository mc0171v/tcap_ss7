package com.vennetics.bell.sam.ss7.tcap.ati.simulator.dialogue.states;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;


@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueStartTest {

    private static final int DIALOGUE_ID = 1111;
    private static final int INVOKE_ID = 34;
    
    public static final byte[] ATI  = { 0x47 };
    
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
    @Mock
    private IComponentRequestBuilder mockRequestBuilder;
    @Mock
    private IDialogueRequestBuilder mockDialogueRequestBuilder;

    private IDialogueState objectToTest;

    @Before
    public void setup() throws Exception {
        objectToTest = new AtiSimDialogueStart(mockDialogueContext, mockDialogue);
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfComponentIndEventNotInvokeInd() throws Exception {
        objectToTest.handleEvent(new ResultIndEvent(mockTcapListener, DIALOGUE_ID, true, true));
    }

    @Test()
    public void shouldHandleIvokeIndEventITU97() throws Exception {
        final Operation operation = new Operation();
        operation.setOperationCode(ATI);
        final InvokeIndEvent invokeIndEvent = new InvokeIndEvent(mockTcapListener, operation, true);
        invokeIndEvent.setInvokeId(INVOKE_ID);
        invokeIndEvent.setDialogueId(DIALOGUE_ID);
        final ResultReqEvent resultReq = new ResultReqEvent(mockTcapListener, DIALOGUE_ID, true);
        final EndReqEvent endReq = new EndReqEvent(mockTcapListener, DIALOGUE_ID);
        when(mockDialogueContext.getTcapEventListener()).thenReturn(mockTcapListener);
        when(mockDialogue.getComponentRequestBuilder()).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.createResultReq(mockTcapListener, DIALOGUE_ID, INVOKE_ID)).thenReturn(resultReq);
        when(mockDialogue.getJainTcapProvider()).thenReturn(mockProvider);
        when(mockDialogue.getDialogueRequestBuilder()).thenReturn(mockDialogueRequestBuilder);
        when(mockDialogueRequestBuilder.createEndReq(mockDialogueContext, DIALOGUE_ID)).thenReturn(endReq);
        when(mockDialogueContext.getDialogueManager()).thenReturn(mockDialogueMgr);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.handleEvent(invokeIndEvent);
        verify(mockProvider).sendComponentReqEventNB(isA(ResultReqEvent.class));
        verify(mockProvider).sendDialogueReqEventNB(isA(EndReqEvent.class));
        verify(mockDialogue).setState(isA(AtiSimDialogueEnd.class));
        verify(mockProvider).releaseDialogueId(DIALOGUE_ID);
        verify(mockDialogueMgr).deactivate(mockDialogue);
    }
    
}
