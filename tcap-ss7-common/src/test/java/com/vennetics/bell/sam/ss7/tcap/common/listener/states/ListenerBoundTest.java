package com.vennetics.bell.sam.ss7.tcap.common.listener.states;

import jain.protocol.ss7.tcap.TcapErrorEvent;
import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.dialogue.BeginIndEvent;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.ericsson.einss7.jtcap.TcStateIndEvent;
import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.IListenerContext;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class ListenerBoundTest {

    private static final int DIALOGUE_ID = 1111;
    private static final short SSN = 8;
    private static final short WRONG_SSN = 9;
    private static final byte[] SPC = {
            0,
            3,
            Tools.getLoByteOf2(231),
    };
    private static final byte[] WRONG_SPC = {
            0,
            3,
            Tools.getLoByteOf2(232),
    };

    @Mock
    private IListenerContext mockListenerContext;
    @Mock
    private TcapEventListener mockTcapListener;

    private IListenerState objectToTest;
    private TcapUserAddress userAddress;

    @Before
    public void setup() throws Exception {
        objectToTest = new ListenerBound(mockListenerContext);
        userAddress = new TcapUserAddress(SPC, SSN);
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfComponentIndEvent() throws Exception {
        final Operation operation = new Operation();
        objectToTest.handleEvent(new InvokeIndEvent(mockTcapListener, operation, true));
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfDialogueIndEvent() throws Exception {
        byte[] byteString = "address".getBytes("UTF-8");
        TcapUserAddress sourceAddress = new TcapUserAddress(byteString, SSN);
        TcapUserAddress destAddress = new TcapUserAddress(byteString, SSN);
        objectToTest.handleEvent(new BeginIndEvent(mockTcapListener,
                                                   DIALOGUE_ID,
                                                   sourceAddress,
                                                   destAddress,
                                                   true));
    }

    @Test()
    public void shouldHandleTcapErrorEvent() throws Exception {
        objectToTest.handleEvent(new TcapErrorEvent(mockTcapListener,
                                                    new RuntimeException("Error")));
        verify(mockListenerContext).cleanup();
        verify(mockListenerContext).clearAllDialogs();
        verify(mockListenerContext).initialise();
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfVendorIndEventNotTcStateInd() throws Exception {
        objectToTest.handleEvent(new TcBindIndEvent(mockTcapListener));
    }

    @Test()
    public void shouldHandleTcStateIndEventReadyForTraffic() throws Exception {
        final TcStateIndEvent tcapStateInd = getTcStateIndEvent();
        final ISs7ConfigurationProperties props = new Ss7ConfigurationProperties();
        props.setWaitForReady(true);
        when(mockListenerContext.getConfigProperties()).thenReturn(props);
        when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
        objectToTest.handleEvent(tcapStateInd);
        verify(mockListenerContext).setState(isA(ListenerReadyForTraffic.class));
    }

    @Test()
    public void shouldHandleTcStateIndEventDifferentSSn() throws Exception {
        final TcStateIndEvent tcapStateInd = getTcStateIndEvent();
        tcapStateInd.setAffectedSsn(WRONG_SSN);
        final ISs7ConfigurationProperties props = new Ss7ConfigurationProperties();
        props.setWaitForReady(true);
        when(mockListenerContext.getConfigProperties()).thenReturn(props);
        when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
        objectToTest.handleEvent(tcapStateInd);
        verify(mockListenerContext, never()).setState(isA(IListenerState.class));
    }

    @Test()
    public void shouldHandleTcStateIndEventDifferentSpc() throws Exception {
        final TcStateIndEvent tcapStateInd = getTcStateIndEvent();
        tcapStateInd.setAffectedSpc(WRONG_SPC);
        final ISs7ConfigurationProperties props = new Ss7ConfigurationProperties();
        props.setWaitForReady(true);
        when(mockListenerContext.getConfigProperties()).thenReturn(props);
        when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
        objectToTest.handleEvent(tcapStateInd);
        verify(mockListenerContext, never()).setState(isA(IListenerState.class));
    }

    @Test()
    public void shouldHandleTcStateIndEventNotReadyForTrafficUserUnavailable() throws Exception {
        final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
        tcapStateInd.setUserStatus(TcStateIndEvent.USER_UNAVAILABLE);
        final ISs7ConfigurationProperties props = new Ss7ConfigurationProperties();
        props.setWaitForReady(true);
        when(mockListenerContext.getConfigProperties()).thenReturn(props);
        when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
        objectToTest.handleEvent(tcapStateInd);
        verify(mockListenerContext, never()).setState(isA(IListenerState.class));
    }
    
    private TcStateIndEvent getTcStateIndEvent() {
        final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
        tcapStateInd.setAffectedSpc(SPC);
        tcapStateInd.setAffectedSsn(SSN);
        return tcapStateInd;
    }
}
