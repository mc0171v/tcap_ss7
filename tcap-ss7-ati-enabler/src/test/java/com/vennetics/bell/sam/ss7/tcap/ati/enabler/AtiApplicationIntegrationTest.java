package com.vennetics.bell.sam.ss7.tcap.ati.enabler;

import com.vennetics.bell.sam.model.location.LocationResponse;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.IAtiService;
import com.vennetics.shared.test.utils.categories.IntegrationTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test Case to verify that we can run up the application and pull sensible
 * values back.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AtiApplication.class)
@WebIntegrationTest({ "server.port:0", "eureka.client.enabled:false",
                "spring.cloud.config.enabled:false", "spring.cloud.config.discovery.enabled:false",
                "security.basic.enabled:false" })  //TODO add properties
@Category(IntegrationTest.class)
public class AtiApplicationIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final RestTemplate restTemplate = new TestRestTemplate();

    // Object under test
    @InjectMocks
    @Autowired
    private AtiController controllerUnderTest;

    // Mock Objects
    @Autowired
    private IAtiService atiService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    // Concrete Objects
    private static final String MSISDN_KEY = "msisdn";
    private static final String MSISDN = "+442890169251";
    private static final String IMSI_KEY = "imsi";
    private static final String IMSI = "123456789012345";
    private static UUID requestId = UUID.randomUUID();

    @Ignore
    @Test
    public void contextLoads() {
    }

    @Ignore
    @Test
    public void testEnvLoads() {
        ResponseEntity<String> entity = restTemplate.getForEntity(
                        "http://localhost:" + port + "/env", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertTrue(entity.getBody().contains("\"spring.application.name\":\"tcap-ss7-ati-enabler\""));
    }

    @Ignore
    @Test
    public void shouldInvokeAtiServiceServiceForLocationRequest() throws Exception {
        when(atiService.sendAtiMessage(isA(UUID.class), isA(OutboundATIMessage.class))).thenReturn(
                        Observable.just(null));
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add(IMSI_KEY, IMSI);
        ResponseEntity<LocationResponse> entity = restTemplate.postForEntity(
                        "http://localhost:" + port + "/outbound/location/requests",
                        requestParams,
                        LocationResponse.class);
        assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
    }


    @Configuration
    public static class TestConfig {

        @Bean
        /**
         * Utility to ensure mocked service is inject into controller rather
         * than the real instance wired in.
         */
        IAtiService atiService() {
            return mock(IAtiService.class);
        }
    }

}
