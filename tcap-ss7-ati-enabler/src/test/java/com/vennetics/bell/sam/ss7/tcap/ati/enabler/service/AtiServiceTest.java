package com.vennetics.bell.sam.ss7.tcap.ati.enabler.service;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.vennetics.bell.sam.error.exceptions.InvalidAddressException;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.common.address.Address;
import com.vennetics.bell.sam.ss7.tcap.common.address.IAddressNormalizer;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.listener.ISamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AtiServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AtiServiceTest.class);

    // Object Under Test
    private IAtiService objectUnderTest;

    // Mock Objects
    @Mock
    private ISamTcapEventListener mockListener;
    @Mock
    private IDialogue mockDialogue;

    @Mock private IAddressNormalizer mockAddressNormalizer;

    // Concrete objects
    private static final String DEST_ADDRESS_ONE = "12345";
    private final UUID externalRequestId = UUID.randomUUID();
    private final Address address = new Address();
    private ISs7ConfigurationProperties props;

    @Before public void setUp() {
        objectUnderTest = new AtiService(mockListener,
                                         mockAddressNormalizer);
        address.setSuppliedAddress(DEST_ADDRESS_ONE);
        address.setE164Address(DEST_ADDRESS_ONE);
        props = new Ss7ConfigurationProperties();
        props.setWaitForReady(true);
    }

    @Test
    public void testThatExceptionThrownForInvalidAddresses() {
        final OutboundATIMessage request = createDefaultOutboundMessage();
        when(mockListener.getConfigProperties()).thenReturn(props);
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

    @Test
    public void testThatAtiMessageCommandExecuted() {
        final OutboundATIMessage request = createDefaultOutboundMessage();
        when(mockAddressNormalizer.normalizeToE164Address(isA(Address.class))).thenReturn(address);
        when(mockListener.getConfigProperties()).thenReturn(props);
        when(mockListener.isBound()).thenReturn(true);
        when(mockListener.isReady()).thenReturn(true);
        when(mockListener.startDialogue(eq(request), isA(CountDownLatch.class))).thenReturn(mockDialogue);
        try {
            objectUnderTest.sendAtiMessage(externalRequestId, request);
            fail("Test should have encountered exception");
        } catch (final HystrixRuntimeException ex) {
            logger.debug("cause {}",ex.getCause().getMessage());
            assertTrue(ex.getCause().getMessage().equals("SS7_SERVICE_EXCEPTION arg[0]=[No result] "));
        }
    }

    private OutboundATIMessage createDefaultOutboundMessage() {
        final OutboundATIMessage outboundMessage = new OutboundATIMessage();
        outboundMessage.setMsisdn(DEST_ADDRESS_ONE);
        return outboundMessage;
    }

}
