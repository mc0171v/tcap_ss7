package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.eq;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.TcapUserAddress;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcAddressIndEvent;
import com.ericsson.einss7.jtcap.TcDialogRelayIndEvent;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;

@RunWith(MockitoJUnitRunner.class)
public class DialogueTest {

    private static final int DIALOGUE_ID = 1111;
    private static final short SSN = 8;

    @Mock
    private BellSamTcapListener mockListener;
    @Mock
    private JainTcapProvider mockProvider;

    private Dialogue objectToTest;

    @Before
    public void setup() {
        objectToTest = new Dialogue(mockListener, DIALOGUE_ID, SSN, mockProvider);
    }

    @Test
    public void shouldHandleVendorDialogueIndEvent() throws Exception {
        byte[] byteString = "address".getBytes("UTF-8");
        TcapUserAddress address = new TcapUserAddress(byteString, SSN);
        TcAddressIndEvent addessEvent = new TcAddressIndEvent("indEvent");
        addessEvent.setAddress(address);
        objectToTest.handleVendorDialogueIndEvent(addessEvent);
        verify(mockListener).setDestinationAddress(eq(address));
    }

    @Test(expected = UnexpectedPrimitiveException.class)
    public void shouldThrowExceptionIfNotTcAddressIndEvent() throws Exception {
        TcDialogRelayIndEvent addessEvent = new TcDialogRelayIndEvent("indEvent");
        objectToTest.handleVendorDialogueIndEvent(addessEvent);
    }

}
