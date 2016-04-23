package com.vennetics.bell.sam.ss7.tcap.ati.enabler.component.requests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;

@RunWith(MockitoJUnitRunner.class)
public class ATIComponentRequestBuilderTest {

    @Mock
    private TcapEventListener mockListener;

    private AtiComponentRequestBuilder objectToTest;
    private ISs7ConfigurationProperties props;

    private static final int INVOKE_ID = 1234;
    private static final int DIALOGUE_ID = 1111;
    private static final long INVOKE_TIMEOUT = 1000;
    private static final String GSM_SCF_ADDRESS = "12344321";
    private static final byte[] OPERATION_ATI = { 0x47 };
    private static final byte[] EXPECTED_IMSI_STRING = { Tools.getLoByteOf2(0xA0), 0x06,
            Tools.getLoByteOf2(0x80), 0x04, 0x21, 0x43, 0x65, Tools.getLoByteOf2(0x87),
            Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00,
            Tools.getLoByteOf2(0x83), 0x04, 0x21, 0x43, 0x34, 0x12 };
    private static final byte[] EXPECTED_MSISDN_STRING = { Tools.getLoByteOf2(0xA0), 0x06,
            Tools.getLoByteOf2(0x81), 0x04, Tools.getLoByteOf2(0x89), 0x67, 0x45,
            Tools.getLoByteOf2(0xF3), Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x81),
            0x00, Tools.getLoByteOf2(0x83), 0x04, 0x21, 0x43, 0x34, 0x12 };
    private static final byte[] EXPECTED_MSISDN_STRING_LOC_STATE = { Tools.getLoByteOf2(0xA0), 0x06,
            Tools.getLoByteOf2(0x81), 0x04, Tools.getLoByteOf2(0x89), 0x67, 0x45,
            Tools.getLoByteOf2(0xF3), Tools.getLoByteOf2(0xA1), 0x04, Tools.getLoByteOf2(0x80),
            0x00, Tools.getLoByteOf2(0x81), 0x00, Tools.getLoByteOf2(0x83), 0x04, 0x21, 0x43, 0x34,
            0x12 };

    @Before
    public void setup() {
        props = new Ss7ConfigurationProperties();
        props.setInvokeTimeout(INVOKE_TIMEOUT);
        props.setGsmScfAddress(GSM_SCF_ADDRESS);
        objectToTest = new AtiComponentRequestBuilder();
    }

    @Test
    public void shouldBuildATIComponentWithImsiAndRequestLocation() throws Exception {
        final InvokeReqEvent result = objectToTest.createInvokeReq(mockListener,
                                                                   INVOKE_ID,
                                                                   getRequestObjectWithImsiAndRequestLocation(),
                                                                   true,
                                                                   props,
                                                                   DIALOGUE_ID);
        check(result, EXPECTED_IMSI_STRING);
    }

    @Test
    public void shouldBuildATIComponentWithMsisdnAndRequestState() throws Exception {
        final InvokeReqEvent result = objectToTest.createInvokeReq(mockListener,
                                                                   INVOKE_ID,
                                                                   getRequestObjectWithMsisdnAndRequestSubscriberState(),
                                                                   true,
                                                                   props,
                                                                   DIALOGUE_ID);
        check(result, EXPECTED_MSISDN_STRING);
    }

    @Test
    public void shouldBuildATIComponentWithMsisdnAndRequestLocationAndState() throws Exception {
        final OutboundATIMessage message = getRequestObjectWithMsisdnAndRequestSubscriberState();
        message.setRequestInfoLocation(true);
        final InvokeReqEvent result = objectToTest.createInvokeReq(mockListener,
                                                                   INVOKE_ID,
                                                                   message,
                                                                   true,
                                                                   props,
                                                                   DIALOGUE_ID);
        check(result, EXPECTED_MSISDN_STRING_LOC_STATE);
    }

    private void check(final InvokeReqEvent result, final byte[] params) throws Exception {
        assertArrayEquals(result.getParameters().getParameter(), params);
        assertArrayEquals(result.getOperation().getOperationCode(), OPERATION_ATI);
        assertTrue(result.getDialogueId() == DIALOGUE_ID);
        assertTrue(result.getInvokeId() == INVOKE_ID);
        assertTrue(result.getOperation().getOperationType() == Operation.OPERATIONTYPE_LOCAL);
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
