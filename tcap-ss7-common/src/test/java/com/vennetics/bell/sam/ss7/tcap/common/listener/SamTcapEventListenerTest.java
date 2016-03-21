package com.vennetics.bell.sam.ss7.tcap.common.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IInitialDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.listener.ISamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.listener.SamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerBound;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerReadyForTraffic;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerUnbound;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.TcapErrorEvent;
import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.dialogue.BeginIndEvent;

@RunWith(MockitoJUnitRunner.class)
public class SamTcapEventListenerTest {
    
    // Object Under Test
    private ISamTcapEventListener objectUnderTest;
    
    //mocks
    @Mock
    private IDialogueManager mockDialogueManager;
    @Mock
    private IInitialDialogueState mockDialogueState;
    @Mock
    private IDialogueRequestBuilder mockDialogueRequestBuilder;
    @Mock
    private IComponentRequestBuilder mockComponentRequestBuilder;
    @Mock
    private IListenerState mockListenerState;
    @Mock
    private JainTcapProvider mockProvider;
    @Mock
    private JainTcapStack mockStack;
    
    private static final short ORIG_SSN = 99;
    private static final short DEST_SSN = 9;
    private static final byte[] ORIG_SPC = {
            Tools.getLoByteOf2(231),
            3,
            0
    };
    private static final byte[] DEST_SPC = {
            Tools.getLoByteOf2(232),
            3,
            0
    };
    private static final int DIALOGUE_ID = 1111;

    private TcapUserAddress origTcapUserAddress;
    private TcapUserAddress destTcapUserAddress;
    private ISs7ConfigurationProperties configProps;
    
    @Before
    public void setup() throws Exception {
        origTcapUserAddress = new TcapUserAddress(ORIG_SPC, ORIG_SSN);
        destTcapUserAddress = new TcapUserAddress(DEST_SPC, DEST_SSN);
        configProps = new Ss7ConfigurationProperties();
        objectUnderTest = new SamTcapEventListener(configProps,
                                                   mockListenerState,
                                                   mockDialogueRequestBuilder,
                                                   mockComponentRequestBuilder,
                                                   mockDialogueState);
        verify(mockListenerState).setContext(objectUnderTest);
    }
    
    @SuppressWarnings("unchecked")
    @Test()
    public void getUserAddressListShouldContainOrigAddress() {
        Vector<TcapUserAddress> addressList = objectUnderTest.getUserAddressList();
        try {
        assertArrayEquals(addressList.get(0).getSignalingPointCode(), ORIG_SPC);
        assertEquals(addressList.get(0).getSubSystemNumber(), ORIG_SSN);
        assertEquals(addressList.size(), 1);
        } catch (Exception ex) {
            fail("Should not throw an exception");
        }
    }
 
    @Test()
    public void shouldStartDialogue() {
        final Object message = new Object();
        CountDownLatch latch = new CountDownLatch(1);
        IDialogue dialogue = objectUnderTest.startDialogue(message, latch);
        verify(mockDialogueState).setContext(objectUnderTest);
        verify(mockDialogueState).setDialogue(dialogue);
        assertEquals(dialogue.getComponentRequestBuilder(), mockComponentRequestBuilder);
        assertEquals(dialogue.getDialogueRequestBuilder(), mockDialogueRequestBuilder);
        assertEquals(dialogue.getState(), mockDialogueState);
        assertEquals(dialogue.getLatch(), latch);
    }
    
    @Test()
    public void shouldCleanUp() {
        objectUnderTest.setProvider(mockProvider);
        objectUnderTest.setStack(mockStack);
        objectUnderTest.cleanup();
        try {
        verify(mockProvider).removeTcapEventListener(objectUnderTest);
        verify(mockStack).deleteProvider(mockProvider);
        } catch (Exception ex) {
            fail("Should not throw exception");
        }
    }
    
    @Test()
    public void shouldClearAllDialogues() {
        objectUnderTest.setDialogueManager(mockDialogueManager);
        objectUnderTest.clearAllDialogs();
        verify(mockDialogueManager).clearAllDialogs();

    }
    
    @Test()
    public void shouldHandOffComponentIndEventToListenerState() {
        final ComponentIndEvent event = new InvokeIndEvent(objectUnderTest, new Operation(), true);
        objectUnderTest.processComponentIndEvent(event);
        verify(mockListenerState).handleEvent(event);
    }

    @Test()
    public void shouldHandOffDialogueIndEventToListenerState() {
        final DialogueIndEvent event = new BeginIndEvent(objectUnderTest,
                                                         DIALOGUE_ID,
                                                         origTcapUserAddress,
                                                         destTcapUserAddress,
                                                         true);
        objectUnderTest.processDialogueIndEvent(event);
        verify(mockListenerState).handleEvent(event);
    }
    
    @Test()
    public void shouldHandOffTcapErrorToListenerState() {
        final TcapErrorEvent event = new TcapErrorEvent(objectUnderTest,
                                                        new RuntimeException("Error"));
        objectUnderTest.processTcapError(event);
        verify(mockListenerState).handleEvent(event);
    }

    @Test()
    public void shouldHandOffVendorIndEventoListenerState() {
        final TcBindIndEvent event = new TcBindIndEvent(objectUnderTest);
        objectUnderTest.processVendorIndEvent(event);
        verify(mockListenerState).handleEvent(event);
    }
 
    @Test()
    public void shouldReturnTrueIfBound() {
        objectUnderTest.setState(new ListenerBound(objectUnderTest));
        assertTrue(objectUnderTest.isBound());
    }
    
    @Test()
    public void shouldReturnFalseIfNotBound() {
        objectUnderTest.setState(new ListenerUnbound(objectUnderTest));
        assertFalse(objectUnderTest.isBound());
    }
    
    @Test()
    public void shouldReturnTrueIfReadyForTraffic() {
        objectUnderTest.setState(new ListenerReadyForTraffic(objectUnderTest));
        assertTrue(objectUnderTest.isBound());
    }
    
    @Test()
    public void shouldReturnFalseIfNotReadyForTraffic() {
        objectUnderTest.setState(new ListenerUnbound(objectUnderTest));
        assertFalse(objectUnderTest.isBound());
    }
    
//    private OutboundATIMessage getRequestObject() {
//        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
//        oBAtiMessage.setImsi("12345678");
//        oBAtiMessage.setRequestInfoLocation(true);
//        return oBAtiMessage;
//    }
}
