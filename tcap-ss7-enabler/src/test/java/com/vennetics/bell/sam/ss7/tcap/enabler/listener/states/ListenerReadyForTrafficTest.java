package com.vennetics.bell.sam.ss7.tcap.enabler.listener.states;


import jain.protocol.ss7.tcap.TcapErrorEvent;
import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.dialogue.BeginIndEvent;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcBindIndEvent;
import com.ericsson.einss7.jtcap.TcStateIndEvent;
import com.ericsson.einss7.jtcap.TcapEventListener;import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IListenerContext;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class ListenerReadyForTrafficTest {
	
    private static final int DIALOGUE_ID = 1111;
    private static final short SSN = 8;
    private static final short WRONG_SSN = 8;
    private static final byte[] SPC = { //signaling point 2143
                                         0,  //id
                                         3, //area
                                         Tools.getLoByteOf2(231),  //zone
                                       };
    private static final byte[] WRONG_SPC = { //signaling point 2143
                                              0,  //id
                                              3, //area
                                              Tools.getLoByteOf2(231),  //zone
                                            };
    
    @Mock
    private IListenerContext mockListenerContext;
    @Mock
    private TcapEventListener mockTcapListener;
    @Mock
    private IDialogue mockDialogue;

    private IListenerState objectToTest;

    @Before
    public void setup() throws Exception {
        objectToTest = new ListenerReadyForTraffic(mockListenerContext);
    }

    @Test()
    public void shouldHandleComponentIndEvent() throws Exception {
        final Operation operation = new Operation();
        InvokeIndEvent event = new InvokeIndEvent(mockTcapListener, operation, true);
        event.setDialogueId(DIALOGUE_ID);
        when(mockListenerContext.getDialogue(DIALOGUE_ID)).thenReturn(mockDialogue);
    	objectToTest.handleEvent(event);
    	verify(mockDialogue).handleEvent(event);
    }
    
    @Test()
    public void shouldHandleDialogueIndEvent() throws Exception {
        byte[] byteString = "address".getBytes("UTF-8");
        TcapUserAddress sourceAddress = new TcapUserAddress(byteString, SSN);
        TcapUserAddress destAddress = new TcapUserAddress(byteString, SSN);
        BeginIndEvent event = new BeginIndEvent(mockTcapListener, DIALOGUE_ID, sourceAddress, destAddress, true);
        event.setDialogueId(DIALOGUE_ID);
        when(mockListenerContext.getDialogue(DIALOGUE_ID)).thenReturn(mockDialogue);
    	objectToTest.handleEvent(event);
    	verify(mockDialogue).handleEvent(event);
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
    	objectToTest.handleEvent(new TcBindIndEvent(mockTcapListener));
    }
    
    @Test()
    public void shouldHandleTcStateIndEventNotReadyForTraffic() throws Exception {
    	final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
    	final TcapUserAddress userAddress = new TcapUserAddress(SPC, SSN);
    	tcapStateInd.setAffectedSpc(SPC);
    	tcapStateInd.setAffectedSsn(SSN);
    	tcapStateInd.setUserStatus(TcStateIndEvent.USER_UNAVAILABLE);
    	when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
    	objectToTest.handleEvent(tcapStateInd);	
    	verify(mockListenerContext).setState(isA(ListenerBound.class));
    }
    
    @Test()
    public void shouldHandleTcStateIndEventNotReadyForTrafficDifferentSSn() throws Exception {
    	final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
    	final TcapUserAddress userAddress = new TcapUserAddress(SPC, SSN);
    	tcapStateInd.setAffectedSpc(SPC);
    	tcapStateInd.setAffectedSsn(WRONG_SSN);
    	tcapStateInd.setUserStatus(TcStateIndEvent.USER_UNAVAILABLE);
    	when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
    	objectToTest.handleEvent(tcapStateInd);	
    }
    
    
    @Test()
    public void shouldHandleTcStateIndEventNotReadyForTrafficDifferentSpc() throws Exception {
    	final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
    	final TcapUserAddress userAddress = new TcapUserAddress(SPC, SSN);
    	tcapStateInd.setAffectedSpc(WRONG_SPC);
    	tcapStateInd.setAffectedSsn(SSN);
    	tcapStateInd.setUserStatus(TcStateIndEvent.USER_UNAVAILABLE);
    	when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
    	objectToTest.handleEvent(tcapStateInd);	
    }
    
    @Test()
    public void shouldHandleTcStateIndEventReadyForTrafficUserAvailable() throws Exception {
    	final TcStateIndEvent tcapStateInd = new TcStateIndEvent(mockTcapListener);
    	final TcapUserAddress userAddress = new TcapUserAddress(SPC, SSN);
    	tcapStateInd.setAffectedSpc(SPC);
    	tcapStateInd.setAffectedSsn(SSN);
    	when(mockListenerContext.getDestinationAddress()).thenReturn(userAddress);
    	objectToTest.handleEvent(tcapStateInd);	
    }
}
