package com.vennetics.bell.sam.ss7.tcap.ati.enabler.dialogue.states;

import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
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
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IInitialDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.common.map.SubscriberState;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueStartTest {

    private static final int DIALOGUE_ID = 1111;
    private static final int LINK_ID = 33;
    private static final int INVOKE_ID = 34;
    private static final short SSN = 8;
    private static final byte[] SPC = {
            Tools.getLoByteOf2(231),
            3,
            0
    };
    
    private static final byte[] PARAM_SUBSCRIBER_STATE = {0x30, 0x06, 0x30, 0x04, Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00 };
    private static final byte[] PARAM_GEO_INFO = {0x30, 0x12, 0x30, 0x10, Tools.getLoByteOf2(0xA0), 0x0E, 0x02, 0x02, 0x01, 0x03, Tools.getLoByteOf2(0x80),
                                                  0x08, 0x10, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x01 };
    private static final byte[] LATITUDE = { 0x00, 0x01, 0x02 }; // From PARAM_GEO_INFO
    private static final byte[] LONGITUDE = { 0x03, 0x04, 0x05 }; // From PARAM_GEO_INFO
    private static final byte UNCERTAINTY = 0x01; // From PARAM_GEO_INFO
    private static final int GEO_AGE = 259; // 0x01, 0x03 From PARAM_GEO_INFO
    private static final byte IDLE = Tools.getLoByteOf2(0x80);
    private static final byte BUSY = Tools.getLoByteOf2(0x81);
    private static final byte DETACHED = Tools.getLoByteOf2(0x0A);
    private static final byte NOT_PROVIDED = Tools.getLoByteOf2(0x82);
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
    private IComponentRequestBuilder mockComponentRequestBuilder;
    @Mock
    private IDialogueRequestBuilder mockDialogueRequestBuilder;
    
    private IInitialDialogueState objectToTest;

    @Before
    public void setup() throws Exception {
        objectToTest = new AtiDialogueStart(mockDialogue);
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfComponentIndEventNotResultInd() throws Exception {
        final Operation operation = new Operation();
        objectToTest.handleEvent(new InvokeIndEvent(mockTcapListener, operation, true));
    }

    @Test
    public void shouldActivateDialogue() throws Exception {
        OutboundATIMessage message = getRequestObject();
        final ISs7ConfigurationProperties props = new Ss7ConfigurationProperties();
        final Operation operation = new Operation();
        final InvokeReqEvent invokeReq = new InvokeReqEvent(mockTcapListener, DIALOGUE_ID, operation);
        final TcapUserAddress origTcapUserAddress = new TcapUserAddress(SPC, SSN);
        final TcapUserAddress destTcapUserAddress = new TcapUserAddress(SPC, SSN);
        BeginReqEvent beginReq = new BeginReqEvent(mockTcapListener, DIALOGUE_ID, origTcapUserAddress, destTcapUserAddress);
        when(mockDialogue.getContext()).thenReturn(mockDialogueContext);
        when(mockDialogueContext.getProvider()).thenReturn(mockProvider);
        when(mockDialogueContext.getSsn()).thenReturn((int) SSN);
        when(mockProvider.getNewDialogueId(SSN)).thenReturn(DIALOGUE_ID);
        when(mockProvider.getNewInvokeId(DIALOGUE_ID)).thenReturn(INVOKE_ID);
        when(mockDialogueContext.getComponentRequestBuilder(objectToTest.getStateType())).thenReturn(mockComponentRequestBuilder);
        when(mockDialogueContext.getTcapEventListener()).thenReturn(mockTcapListener);
        when(mockDialogue.getRequest()).thenReturn(message);
        when(mockDialogueContext.getConfigProperties()).thenReturn(props);
        when(mockComponentRequestBuilder.createInvokeReq(mockTcapListener,
                                                         INVOKE_ID,
                                                         message,
                                                         true,
                                                         props,
                                                         DIALOGUE_ID)).thenReturn(invokeReq);
        when(mockDialogueContext.getDialogueRequestBuilder(null)).thenReturn(mockDialogueRequestBuilder);
        when(mockDialogueRequestBuilder.createBeginReq(mockDialogueContext, DIALOGUE_ID)).thenReturn(beginReq);
        objectToTest.activate();
        verify(mockDialogue).setDialogueId(DIALOGUE_ID);
        verify(mockProvider).sendComponentReqEventNB(invokeReq);
        verify(mockProvider).sendDialogueReqEventNB(beginReq);
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
    public void shouldProcessReturnedBytesWithSubscriberStateIdle() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_SUBSCRIBER_STATE, IDLE);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.ASSUMED_IDLE);
    }
    
    @Test
    public void shouldProcessReturnedBytesWithSubscriberStateBusy() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_SUBSCRIBER_STATE, BUSY);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.CAMEL_BUSY);
    }
    
    @Test
    public void shouldProcessReturnedBytesWithSubscriberStateDetached() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_SUBSCRIBER_STATE, DETACHED);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.NET_DET_NOT_REACHEABLE);
    }
    
    @Test
    public void shouldProcessReturnedBytesWithSubscriberStateNotProvided() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_SUBSCRIBER_STATE, NOT_PROVIDED);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getStatus() == SubscriberState.NOT_PROVIDED_VLR);
    }
    
    @Test
    public void shouldProcessReturnedBytesWithLocationInfo() {
        final ResultIndEvent resultIndEvent = commonSetup(PARAM_GEO_INFO, (byte) 0x00);
        OutboundATIMessage oAtiMessage = getRequestObject();
        commonWhenItu(oAtiMessage);
        objectToTest.handleEvent(resultIndEvent);
        commonVerifyItu(oAtiMessage);
        assertTrue(oAtiMessage.getAge() == GEO_AGE);
        assertTrue(oAtiMessage.getUncertainty() == UNCERTAINTY);
        assertArrayEquals(oAtiMessage.getLatitude(), LATITUDE);
        assertArrayEquals(oAtiMessage.getLongitude(), LONGITUDE);
    }
    
    private ResultIndEvent commonSetup(final byte[] bs, final byte state) {
        byte[] newbs = bs;
        if (state != (byte) 0x00) {
            newbs[6] = state;
        }
        final ResultIndEvent resultIndEvent = new ResultIndEvent(mockTcapListener,
                                                                 DIALOGUE_ID,
                                                                 true,
                                                                 true);
        if (bs != null) {
            Parameters params = new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, newbs);
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
        when(mockDialogue.getContext()).thenReturn(mockDialogueContext);
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
