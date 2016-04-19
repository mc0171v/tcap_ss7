package com.vennetics.bell.sam.ss7.tcap.common.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.eq;


import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IInitialDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerBound;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerReadyForTraffic;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerUnbound;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties.Ss7Address;

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
    
    private static final Logger logger = LoggerFactory.getLogger(SamTcapEventListenerTest.class);
    
    // Object Under Test
    private ISamTcapEventListener objectUnderTest;
    
    //mocks
    @Mock
    private IDialogueManager mockDialogueManager;
    @Mock
    private IInitialDialogueState mockDialogueState;
    @Mock
    private IInitialDialogueState mockDialogueState2;
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
    private static final long LONG_ORIG_SPC = 999;
    private static final long LONG_DEST_SPC = 1000;
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
        final Ss7Address destAddress = new Ss7Address();
        destAddress.setSpc(LONG_DEST_SPC);
        destAddress.setSsn(DEST_SSN);
        final Ss7Address origAddress = new Ss7Address();
        origAddress.setSpc(LONG_ORIG_SPC);
        origAddress.setSsn(ORIG_SSN);
        configProps.setDestAddress(destAddress);
        configProps.setOrigAddress(origAddress);
        objectUnderTest = new SamTcapEventListener(configProps,
                                                   mockListenerState,
                                                   mockDialogueRequestBuilder,
                                                   mockComponentRequestBuilder,
                                                   mockDialogueState) {
            public JainTcapStack createJainTcapStack() throws jain.protocol.ss7.SS7PeerUnavailableException,
            jain.protocol.ss7.tcap.TcapException {
                logger.debug("Reutning mock stack");
                return mockStack;
            }
        };
        verify(mockListenerState).setContext(objectUnderTest);
    }
    
    @Test
    public void shouldInitialiseSuccessfully() throws Exception {
        when(mockStack.createAttachedProvider()).thenReturn(mockProvider);
        objectUnderTest.initialise();
        verify(mockProvider).addTcapEventListener(eq(objectUnderTest), argThat(new ArgumentMatcher<TcapUserAddress>() {
            @Override
            public boolean matches(final Object argument) {
                try {
              assertArrayEquals(ORIG_SPC, ((TcapUserAddress) argument).getSignalingPointCode());
              assertEquals(ORIG_SSN, ((TcapUserAddress) argument).getSubSystemNumber());
              return true;
                } catch (Exception ex) {
                    return false;
                }
            }
          }));
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
        when(mockDialogueState.newInstance()).thenReturn(mockDialogueState2);
        IDialogue dialogue = objectUnderTest.startDialogue(message, latch);
        verify(mockDialogueState2).setContext(objectUnderTest);
        verify(mockDialogueState2).setDialogue(dialogue);
        assertEquals(dialogue.getComponentRequestBuilder(), mockComponentRequestBuilder);
        assertEquals(dialogue.getDialogueRequestBuilder(), mockDialogueRequestBuilder);
        assertEquals(dialogue.getState(), mockDialogueState2);
        assertEquals(dialogue.getLatch(), latch);
    }
    
    @Test()
    public void shouldJoinDialogue() {
        when(mockDialogueState.newInstance()).thenReturn(mockDialogueState2);
        IDialogue dialogue = objectUnderTest.joinDialogue(DIALOGUE_ID);
        verify(mockDialogueState2).setContext(objectUnderTest);
        verify(mockDialogueState2).setDialogue(dialogue);
        assertEquals(dialogue.getComponentRequestBuilder(), mockComponentRequestBuilder);
        assertEquals(dialogue.getDialogueRequestBuilder(), mockDialogueRequestBuilder);
        assertEquals(dialogue.getState(), mockDialogueState2);
        assertEquals(dialogue.getDialogueId(), DIALOGUE_ID);
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
}
