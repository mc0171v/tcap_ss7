package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;


@RunWith(MockitoJUnitRunner.class)
public class ATIComponentRequestBuilderTest {

    private ATIComponentRequestBuilder objectToTest;

    @Before
    public void setup() {
        objectToTest = new ATIComponentRequestBuilder();
    }

    @Test
    public void shouldHandleDialogueIndEvent() throws Exception {
        final byte[] result = objectToTest.createParameters(getRequestObject());
        System.out.println("result = " + bytesToHex(result));
    }
       
    private OutboundATIMessage getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }

    public static String bytesToHex(final byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
