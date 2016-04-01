package com.vennetics.bell.sam.ss7.tcap.ati.simulator.component.requests;


import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;


import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;

import jain.protocol.ss7.tcap.component.ResultReqEvent;


@RunWith(MockitoJUnitRunner.class)
public class ATIComponentRequestBuilderTest {

    private AtiComponentRequestBuilder objectToTest;
    
    private static final int DIALOGUE_ID = 1;
    private static final int INVOKE_ID = 2;
    public static final byte[] ATI  = { 0x47 };
    
    @Mock
    private IDialogueContext mockContext;
    @Mock
    private TcapEventListener mockListener;
    
    @Before
    public void setup() {
        objectToTest = new AtiComponentRequestBuilder();
    }

    @Test
    public void shouldBuildATIResultReq() throws Exception {
        when(mockContext.getTcapEventListener()).thenReturn(mockListener);
        final ResultReqEvent result = objectToTest.createResultReq(mockContext, DIALOGUE_ID, INVOKE_ID);
        assertArrayEquals(result.getOperation().getOperationCode(), ATI);
    }

}
