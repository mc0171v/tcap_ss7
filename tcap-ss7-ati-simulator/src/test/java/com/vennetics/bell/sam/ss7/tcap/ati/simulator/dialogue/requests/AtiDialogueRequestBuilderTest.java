package com.vennetics.bell.sam.ss7.tcap.ati.simulator.dialogue.requests;


import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.dialogue.EndReqEvent;


@RunWith(MockitoJUnitRunner.class)
public class AtiDialogueRequestBuilderTest {
    
    private static final int DIALOGUE_ID = 1111;
    private static final byte[] ANYTIMEINFOENQUIRYCONTEXT_V3 = { 0x06, 0x07, 0x04, 0x00, 0x00,  0x01, 0x00, 0x29, 0x03 };
    
    @Mock
    private IDialogueContext mockContext;
    @Mock
    private TcapEventListener mockListener;
    
    private AtiDialogueRequestBuilder objectToTest;
    
    @Before
    public void setup() {
        objectToTest = new AtiDialogueRequestBuilder();
    }

    @Test
    public void shouldBuildEndReq() throws Exception {
        when(mockContext.getTcapEventListener()).thenReturn(mockListener);
        final EndReqEvent result = objectToTest.createEndReq(mockContext, DIALOGUE_ID);
        assertArrayEquals(result.getDialoguePortion().getAppContextName(), ANYTIMEINFOENQUIRYCONTEXT_V3);
    }

}
