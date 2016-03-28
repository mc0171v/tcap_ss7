package com.vennetics.bell.sam.ss7.tcap.ati.enabler.dialogue.states;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;

import static org.junit.Assert.assertArrayEquals;
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
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.map.SubscriberState;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueStartTest {

    private static final int DIALOGUE_ID = 1111;
    private static final int LINK_ID = 33;
    private static final int INVOKE_ID = 34;
    
    private static final byte[] PARAM_SUBSCRIBER_STATE = {0x30, 0x06, 0x30, 0x04, Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00 };
    private static final byte[] PARAM_GEO_INFO = {0x30, 0x12, 0x30, 0x10, Tools.getLoByteOf2(0xA0), 0x0E, 0x02, 0x02, 0x01, 0x03, Tools.getLoByteOf2(0x80),
                                                  0x08, 0x10, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x01 };
    private static final byte[] LATITUDE = { 0x00, 0x01, 0x02 }; // From PARAM_GEO_INFO
    private static final byte[] LONGITUDE = { 0x03, 0x04, 0x05 }; // From PARAM_GEO_INFO
    private static final byte UNCERTAINTY = 0x01; // From PARAM_GEO_INFO
    private static final int GEO_AGE = 259; // 0x01, 0x03 From PARAM_GEO_INFO
    
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
        commonWhenAnsi(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyAnsi(oAtiMessage);
        assertTrue(oAtiMessage.getError() != null);
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
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getError() != null);

    }
    
    @Test
    public void shouldProcessReturnedBytesWithSubscriberState() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_SUBSCRIBER_STATE);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.ASSUMED_IDLE);
    }
    
    @Test
    public void shouldProcessReturnedBytesWithLocationInfo() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_GEO_INFO);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getAge() == GEO_AGE);
        assertTrue(oAtiMessage.getUncertainty() == UNCERTAINTY);
        assertArrayEquals(oAtiMessage.getLatitude(), LATITUDE);
        assertArrayEquals(oAtiMessage.getLongitude(), LONGITUDE);
    }
    
    private ResultIndEvent commonSetup(final byte[] bs) {
        final ResultIndEvent resultIndEvent = new ResultIndEvent(mockTcapListener,
                                                                 DIALOGUE_ID,
                                                                 true,
                                                                 true);
        if (bs != null) {
            Parameters params = new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, bs);
            resultIndEvent.setParameters(params);
        }
        resultIndEvent.setInvokeId(INVOKE_ID);
        resultIndEvent.setDialogueId(DIALOGUE_ID);
        return resultIndEvent;
    };
    
    private void commonWhenItu(final OutboundATIMessage oAtiMessage) {
        when(mockStack.getProtocolVersion()).thenReturn(DialogueConstants.PROTOCOL_VERSION_ITU_97);
        commonWhen(oAtiMessage);
    }
    
    private void commonWhenAnsi(final OutboundATIMessage oAtiMessage) {
        when(mockStack.getProtocolVersion()).thenReturn(DialogueConstants.PROTOCOL_VERSION_ANSI_96);
        commonWhen(oAtiMessage);
    }
    
    private void commonWhen(final OutboundATIMessage oAtiMessage) {
        when(mockDialogue.getRequest()).thenReturn(oAtiMessage);
        when(mockDialogueContext.getStack()).thenReturn(mockStack);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockDialogueContext.getDialogue(DIALOGUE_ID)).thenReturn(mockDialogue);
        when(mockDialogueContext.getDialogueManager()).thenReturn(mockDialogueMgr);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
    }
    
    private void commonVerifyItu(final OutboundATIMessage oAtiMessage) {
        verify(mockProvider).releaseInvokeId(INVOKE_ID, DIALOGUE_ID);
        commonVerify(oAtiMessage);
    }
    
    private void commonVerifyAnsi(final OutboundATIMessage oAtiMessage) {
        verify(mockProvider).releaseInvokeId(LINK_ID, DIALOGUE_ID);
        commonVerify(oAtiMessage);
    }
    
    private void commonVerify(final OutboundATIMessage oAtiMessage) {
        verify(mockDialogue).setState(isA(AtiDialogueEnd.class));
        verify(mockDialogue).setResult(oAtiMessage);
        verify(mockProvider).releaseDialogueId(DIALOGUE_ID);
        verify(mockDialogueMgr).deactivate(mockDialogue);
    }
    
    private OutboundATIMessage getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }
}
