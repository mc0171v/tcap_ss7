package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;

import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.enabler.map.ati.SubscriberState;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueStartTest {

    private static final int DIALOGUE_ID = 1111;
    private static final int LINK_ID = 33;
    private static final int INVOKE_ID = 34;
    
    private static final byte[] PARAMETER_SEQ = {0x30, 0x06, 0x30, 0x04, Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00 };
    
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

    private IDialogueState objectToTest;

    @Before
    public void setup() throws Exception {
        objectToTest = new AtiDialogueStart(mockDialogueContext, mockDialogue);
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfComponentIndEventNotResultInd() throws Exception {
        final Operation operation = new Operation();
        objectToTest.handleEvent(new InvokeIndEvent(mockTcapListener, operation, true));
    }

    @Test()
    public void shouldHandleResultIndEventAnsi96() throws Exception {
        final ResultIndEvent resultIndEvent = new ResultIndEvent(mockTcapListener,
                                                                 DIALOGUE_ID,
                                                                 true,
                                                                 true);
        resultIndEvent.setLinkId(LINK_ID);
        resultIndEvent.setDialogueId(DIALOGUE_ID);
        OutboundATIMessage oAtiMessage = getRequestObject();
        when(mockDialogue.getRequest()).thenReturn(oAtiMessage);
        when(mockDialogueContext.getStack()).thenReturn(mockStack);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockStack.getProtocolVersion()).thenReturn(DialogueConstants.PROTOCOL_VERSION_ANSI_96);
        when(mockDialogueContext.getDialogue(DIALOGUE_ID)).thenReturn(mockDialogue);
        objectToTest.handleEvent(resultIndEvent);
        verify(mockProvider).releaseInvokeId(LINK_ID, DIALOGUE_ID);
        verify(mockDialogue).setState(isA(AtiDialogueEnd.class));
        verify(mockDialogue).setResult(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.UNKOWN.ordinal()); //TODO change to real status
    }

    @Test()
    public void shouldHandleResultIndEventITU97() throws Exception {
        final ResultIndEvent resultIndEvent = new ResultIndEvent(mockTcapListener,
                                                                 DIALOGUE_ID,
                                                                 true,
                                                                 true);
        resultIndEvent.setInvokeId(INVOKE_ID);
        resultIndEvent.setDialogueId(DIALOGUE_ID);
        OutboundATIMessage oAtiMessage = getRequestObject();
        when(mockDialogue.getRequest()).thenReturn(oAtiMessage);
        when(mockDialogueContext.getStack()).thenReturn(mockStack);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockStack.getProtocolVersion()).thenReturn(DialogueConstants.PROTOCOL_VERSION_ITU_97);
        when(mockDialogueContext.getDialogue(DIALOGUE_ID)).thenReturn(mockDialogue);
        objectToTest.handleEvent(resultIndEvent);
        verify(mockProvider).releaseInvokeId(INVOKE_ID, DIALOGUE_ID);
        verify(mockDialogue).setState(isA(AtiDialogueEnd.class));
        verify(mockDialogue).setResult(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.UNKOWN.ordinal()); //TODO change to real status

    }
    
    @Test
    public void shouldProcessReturnedBytes() {
        final ResultIndEvent resultIndEvent = new ResultIndEvent(mockTcapListener,
                                                                 DIALOGUE_ID,
                                                                 true,
                                                                 true);
        Parameters params = new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, PARAMETER_SEQ);
        resultIndEvent.setParameters(params);
        resultIndEvent.setInvokeId(INVOKE_ID);
        resultIndEvent.setDialogueId(DIALOGUE_ID);
        OutboundATIMessage oAtiMessage = getRequestObject();
        when(mockDialogue.getRequest()).thenReturn(oAtiMessage);
        when(mockDialogueContext.getStack()).thenReturn(mockStack);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockStack.getProtocolVersion()).thenReturn(DialogueConstants.PROTOCOL_VERSION_ITU_97);
        when(mockDialogueContext.getDialogue(DIALOGUE_ID)).thenReturn(mockDialogue);
        objectToTest.handleEvent(resultIndEvent);
        verify(mockProvider).releaseInvokeId(INVOKE_ID, DIALOGUE_ID);
        verify(mockDialogue).setState(isA(AtiDialogueEnd.class));
        assertTrue(oAtiMessage.getStatus() == SubscriberState.ASSUMED_IDLE.ordinal());
    }
    
    private OutboundATIMessage getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }
}
