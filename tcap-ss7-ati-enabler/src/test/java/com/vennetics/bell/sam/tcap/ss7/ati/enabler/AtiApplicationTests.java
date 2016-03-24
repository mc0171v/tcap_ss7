package com.vennetics.bell.sam.tcap.ss7.ati.enabler;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.vennetics.bell.sam.ss7.tcap.ati.enabler.AtiApplication;
import com.vennetics.shared.test.utils.categories.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AtiApplication.class)
@WebIntegrationTest({ "server.port:0", "eureka.client.enabled:false",
        "spring.cloud.config.enabled:false", "spring.cloud.config.discovery.enabled:false",
        "security.basic.enabled:false" })
@Category(IntegrationTest.class)
public class AtiApplicationTests {

    @Value("${local.server.port}")
    private int port;

    private final RestTemplate restTemplate = new TestRestTemplate();

    @Ignore @Test
    public void contextLoads() {
    }

    @Ignore @Test
    public void testDefaultResponseFromHelloworldEndpoint() {
        final ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port
                        + "/helloss7", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
