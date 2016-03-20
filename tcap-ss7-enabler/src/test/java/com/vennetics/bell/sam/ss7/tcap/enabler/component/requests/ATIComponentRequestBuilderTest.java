package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;


import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import ericsson.ein.ss7.commonparts.util.Tools;


@RunWith(MockitoJUnitRunner.class)
public class ATIComponentRequestBuilderTest {

    private AtiComponentRequestBuilder objectToTest;
    
    private static final byte[] EXPECTED_IMSI_STRING = { Tools.getLoByteOf2(0xA0), 0x06, Tools.getLoByteOf2(0x80), 0x04, 0x21, 0x43, 0x65, Tools.getLoByteOf2(0x87),
            Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00, Tools.getLoByteOf2(0x83), 0x04, 0x21, 0x43, 0x34, 0x12};
    private static final byte[] EXPECTED_MSISDN_STRING = { Tools.getLoByteOf2(0xA0), 0x06, Tools.getLoByteOf2(0x81), 0x04, Tools.getLoByteOf2(0x89), 0x67, 0x45,
            Tools.getLoByteOf2(0xF3), Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x81), 0x00, Tools.getLoByteOf2(0x83), 0x04, 0x21, 0x43, 0x34, 0x12};
    @Before
    public void setup() {
        objectToTest = new AtiComponentRequestBuilder();
    }

    @Test
    public void shouldBuildATIComponentWithImsi() throws Exception {
        final byte[] result = objectToTest.createParameters(getRequestObjectWithImsiAndRequestLocation());
        
        assertArrayEquals(result, EXPECTED_IMSI_STRING);
    }
 
    @Test
    public void shouldBuildATIComponentWithMsisdn() throws Exception {
        final byte[] result = objectToTest.createParameters(getRequestObjectWithMsisdnAndRequestSubscriberState());
        assertArrayEquals(result, EXPECTED_MSISDN_STRING);
    }
    
    private OutboundATIMessage getRequestObjectWithImsiAndRequestLocation() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }
    
    private OutboundATIMessage getRequestObjectWithMsisdnAndRequestSubscriberState() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setMsisdn("9876543");
        oBAtiMessage.setRequestInfoSubscriberState(true);
        return oBAtiMessage;
    }
}
