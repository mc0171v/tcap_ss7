package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.requests;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.TcapUserAddress;
import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;


@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueRequestBuilderTest {
    
    private static final int DIALOGUE_ID = 1111;
    private static final byte[] ANYTIMEINFOENQUIRYCONTEXT_V3 = { 0x06, 0x07, 0x04, 0x00, 0x00,  0x01, 0x00, 0x29, 0x03 };
    private static final short SSN = 8;
    
    @Mock
    private IDialogueContext mockContext;
    @Mock
    private TcapEventListener mockListener;

    private TcapUserAddress origAddress;

    private TcapUserAddress destAddress;
    
    private AtiDialogueRequestBuilder objectToTest;
    
    @Before
    public void setup() {
        objectToTest = new AtiDialogueRequestBuilder();
    }

    @Test
    public void shouldBuildATIComponentWithImsi() throws Exception {
        byte[] oByteString = "origAddress".getBytes("UTF-8");
        byte[] dByteString = "destAddress".getBytes("UTF-8");
        origAddress = new TcapUserAddress(oByteString, SSN);
        destAddress = new TcapUserAddress(dByteString, SSN);
        when(mockContext.getTcapEventListener()).thenReturn(mockListener);
        when(mockContext.getOrigAddr()).thenReturn(origAddress);
        when(mockContext.getDestAddr()).thenReturn(destAddress);
        final BeginReqEvent result = objectToTest.createBeginReq(mockContext, DIALOGUE_ID);
        assertEquals(result.getOriginatingAddress(), origAddress);
        assertEquals(result.getDestinationAddress(), destAddress);
        assertArrayEquals(result.getDialoguePortion().getAppContextName(), ANYTIMEINFOENQUIRYCONTEXT_V3);
    }

}
