package com.vennetics.bell.sam.ss7.tcap.enabler.listener.states;


import jain.protocol.ss7.tcap.TcapErrorEvent;
import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.dialogue.BeginIndEvent;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.ericsson.einss7.jtcap.TcStateIndEvent;
import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IListenerContext;

@RunWith(MockitoJUnitRunner.class)
public class ListenerUnboundTest {
	
    private static final int DIALOGUE_ID = 1111;
    private static final short SSN = 8;

    @Mock
    private IListenerContext mockListenerContext;
    @Mock
    private TcapEventListener mockTcapListener;

    private IListenerState objectToTest;

    @Before
    public void setup() {
        objectToTest = new ListenerUnbound(mockListenerContext);
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
    	objectToTest.handleEvent(new BeginIndEvent(mockTcapListener, DIALOGUE_ID, sourceAddress, destAddress, true));
    }

    @Test()
    public void shouldHandleTcapErrorEvent() throws Exception {
        objectToTest.handleEvent(new TcapErrorEvent(mockTcapListener, new RuntimeException("Error")));
        verify(mockListenerContext).cleanup();
        verify(mockListenerContext).clearAllDialogs();
        verify(mockListenerContext).initialise(false);
    }
    
    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfVendorIndEventNotTcStateInd() throws Exception {
        final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
    	objectToTest.handleEvent(tcapStateInd);
    }
    
    @Test()
    public void shouldHandleTcBindIndEvent() throws Exception {
    	objectToTest.handleEvent(new TcBindIndEvent(mockTcapListener));
    	verify(mockListenerContext).setState(isA(ListenerBound.class));
    }
}
