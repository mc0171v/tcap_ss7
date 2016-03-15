package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import com.vennetics.bell.sam.error.exceptions.InvalidAddressException;
import com.vennetics.bell.sam.ss7.tcap.enabler.address.Address;
import com.vennetics.bell.sam.ss7.tcap.enabler.address.IAddressNormalizer;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class AtiServiceTest {

    // Object Under Test
    private IAtiService objectUnderTest;

    // Mock Objects
    @Mock
    private IBellSamTcapEventListener mockListener;

    @Mock private IAddressNormalizer mockAddressNormalizer;

    // Concrete objects
    private static final String SENDER_ADDRESS = "123456";
    private static final String DEST_ADDRESS_ONE = "12345";
    private final String messageText = "Test Message";
    private final UUID externalRequestId = UUID.randomUUID();
    private final Address address = new Address();
    private final String notifyUrl = "http://bbc.co.uk";
    private final String correlator = "abc123";

    @Before public void setUp() {
        objectUnderTest = new AtiService(mockListener,
                                         mockAddressNormalizer);
        address.setSuppliedAddress(DEST_ADDRESS_ONE);
        address.setE164Address(DEST_ADDRESS_ONE);
    }

    @Test public void testThatExceptionThrownForInvalidAddresses() {
        final OutboundATIMessage request = createDefaultOutboundMessage();
        when(mockListener.isBound()).thenReturn(true);
        when(mockListener.isReady()).thenReturn(true);
        when(mockAddressNormalizer.normalizeToE164Address(isA(Address.class))).thenThrow(new InvalidAddressException(
                        DEST_ADDRESS_ONE));
        try {
            objectUnderTest.sendAtiMessage(externalRequestId, request);
            fail("Test should have encountered exception");
        } catch (final InvalidAddressException iae) {
            assertArrayEquals(iae.getMessageArguments(), new String[] { DEST_ADDRESS_ONE });
        }
    }

    @Test public void testThatAtiMessageSent() {
        final OutboundATIMessage request = createDefaultOutboundMessage();
        when(mockAddressNormalizer.normalizeToE164Address(isA(Address.class))).thenReturn(address);
        when(mockListener.isBound()).thenReturn(true);
        when(mockListener.isReady()).thenReturn(true);
        objectUnderTest.sendAtiMessage(externalRequestId, request);

    }

    private OutboundATIMessage createDefaultOutboundMessage() {
        final OutboundATIMessage outboundMessage = new OutboundATIMessage();
        outboundMessage.setMsisdn(DEST_ADDRESS_ONE);
        return outboundMessage;
    }

}
