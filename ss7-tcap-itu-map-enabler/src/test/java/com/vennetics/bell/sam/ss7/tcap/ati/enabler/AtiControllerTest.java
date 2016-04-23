package com.vennetics.bell.sam.ss7.tcap.ati.enabler;

import com.vennetics.bell.sam.model.location.LocationResponse;
import com.vennetics.bell.sam.model.subscriber.status.SubscriberStatusResponse;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.IAtiService;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.map.SubscriberState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AtiControllerTest {

    // Object under test
    private AtiController objectUnderTest;

    // Mock/Spy Objects
    @Mock
    private IAtiService mockAtiService;
    @Mock
    private HttpServletResponse mockServletResponse;

    // Concrete Objects
    private static final String MSISDN_KEY = "msisdn";
    private static final String MSISDN = "+442890169251";
    private static final String IMSI_KEY = "imsi";
    private static final String IMSI = "123456789012345";
    private static final String SS7_EXCEPTION_MESSAGE = "SS7_SERVICE_EXCEPTION arg[0]=[ss7 exception] ";
    private static final String SS7_EXCEPTION_ERROR = "ss7 exception";
    private static final byte[] LATITUDE = { 0x01, 0x02, 0x03 };
    private static final byte[] LONGITUDE = { 0x11, 0x12, 0x13 };
    private static final byte UNCERTAINTY = 0x21;

    @Before
    public void setUp() {
        objectUnderTest = new AtiController();
        ReflectionTestUtils.setField(objectUnderTest, "atiService", mockAtiService);
    }

    @Test
    public void testExceptionFromAtiService() {
        when(mockAtiService.sendAtiMessage(isA(UUID.class),
                                           isA(OutboundATIMessage.class))).thenThrow(new Ss7ServiceException(SS7_EXCEPTION_ERROR));
        try {
            objectUnderTest.sendLocationRequest(createImsiMap());
            fail("Exception should have been thrown and this point should not be reached");
        } catch (final Ss7ServiceException ss7ex) {
            assertEquals(ss7ex.getMessage(), SS7_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void testLocationImsiRequest() {
        when(mockAtiService.sendAtiMessage(isA(UUID.class),
                                           argThat(new ArgumentMatcher<OutboundATIMessage>() {
                                               @Override
                                               public boolean matches(final Object argument) {
                                                   assertTrue(((OutboundATIMessage) argument).getRequestInfoLocation());
                                                   assertEquals(IMSI,
                                                                ((OutboundATIMessage) argument).getImsi());
                                                   return true;
                                               }
                                           }))).thenReturn(Observable.just(getRequestObject()));
        final ResponseEntity<LocationResponse> result = objectUnderTest.sendLocationRequest(createImsiMap());
        checkLocation(result);
    }

    @Test
    public void testLocationMsisdnRequest() {
        when(mockAtiService.sendAtiMessage(isA(UUID.class),
                                           argThat(new ArgumentMatcher<OutboundATIMessage>() {
                                               @Override
                                               public boolean matches(final Object argument) {
                                                   assertTrue(((OutboundATIMessage) argument).getRequestInfoLocation());
                                                   assertEquals(MSISDN,
                                                                ((OutboundATIMessage) argument).getMsisdn());
                                                   return true;
                                               }
                                           }))).thenReturn(Observable.just(getRequestObject()));
        final ResponseEntity<LocationResponse> result = objectUnderTest.sendLocationRequest(createMsisdnMap());
        checkLocation(result);
    }

    @Test
    public void testStatusImsiRequest() {
        when(mockAtiService.sendAtiMessage(isA(UUID.class),
                                           argThat(new ArgumentMatcher<OutboundATIMessage>() {
                                               @Override
                                               public boolean matches(final Object argument) {
                                                   assertTrue(((OutboundATIMessage) argument).getRequestInfoSubscriberState());
                                                   assertEquals(IMSI,
                                                                ((OutboundATIMessage) argument).getImsi());
                                                   return true;
                                               }
                                           }))).thenReturn(Observable.just(getRequestObject()));
        final ResponseEntity<SubscriberStatusResponse> result = objectUnderTest.sendStatusRequest(createImsiMap());
        checkStatus(result);
    }

    @Test
    public void testStatusMsisdnRequest() {
        when(mockAtiService.sendAtiMessage(isA(UUID.class),
                                           argThat(new ArgumentMatcher<OutboundATIMessage>() {
                                               @Override
                                               public boolean matches(final Object argument) {
                                                   assertTrue(((OutboundATIMessage) argument).getRequestInfoSubscriberState());
                                                   assertEquals(MSISDN,
                                                                ((OutboundATIMessage) argument).getMsisdn());
                                                   return true;
                                               }
                                           }))).thenReturn(Observable.just(getRequestObject()));
        final ResponseEntity<SubscriberStatusResponse> result = objectUnderTest.sendStatusRequest(createMsisdnMap());
        checkStatus(result);
    }

    private void checkLocation(final ResponseEntity<LocationResponse> result) {
        assertNotNull(result);
        assertTrue(result.getStatusCode() == HttpStatus.OK);
        double expectedLatitude = 90.0 * 66051.0 / Math.pow(2, 23);
        double expectedLongitude = 360.0 * 1118739.0 / Math.pow(2, 24);
        double expectedUncertainty = 222.3;
        assertEquals(result.getBody().getLatitude(), expectedLatitude, 1 / Math.pow(2, 25));
        assertEquals(result.getBody().getLongitude(), expectedLongitude, 1 / Math.pow(2, 25));
        assertEquals(result.getBody().getUncertainty(),
                     expectedUncertainty,
                     expectedUncertainty * 0.01);
    }

    private void checkStatus(final ResponseEntity<SubscriberStatusResponse> result) {
        assertNotNull(result);
        assertTrue(result.getStatusCode() == HttpStatus.OK);
        assertEquals(SubscriberState.CAMEL_BUSY.name(), result.getBody().getSubscriberStatus());
    }

    private static MultiValueMap<String, String> createImsiMap() {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add(IMSI_KEY, IMSI);
        return map;
    }

    private static MultiValueMap<String, String> createMsisdnMap() {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add(MSISDN_KEY, MSISDN);
        return map;
    }

    private OutboundATIMessage getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi(IMSI);
        oBAtiMessage.setRequestInfoLocation(true);
        oBAtiMessage.setLatitude(LATITUDE);
        oBAtiMessage.setLongitude(LONGITUDE);
        oBAtiMessage.setUncertainty(UNCERTAINTY);
        oBAtiMessage.setStatus(SubscriberState.CAMEL_BUSY);
        return oBAtiMessage;
    }
}
