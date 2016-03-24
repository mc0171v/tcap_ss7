package com.vennetics.bell.sam.terminal.location.commands;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;


import com.vennetics.bell.sam.model.location.LocationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.vennetics.bell.sam.terminal.location.TestConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class SendLocationRequestCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(SendLocationRequestCommandTest.class);

    @Mock
    private RestTemplate mockTemplate;
//    @Mock
//    private SimpleClientHttpRequestFactory mockRf;

    @Test
    public void shouldSendLocationRequest() throws Exception {
        final String expectedUrl = String.format("http://tcap-ss7-ati-enabler/outbound/location/requests?imsi=%s", TestConfiguration.IMSI);
        LOG.debug(expectedUrl);
        MultiValueMap<String, String> map = TestConfiguration.createMap();
        ResponseEntity<LocationResponse> response = TestConfiguration.dummyResponse();
//        when(mockTemplate.getRequestFactory()).thenReturn(mockRf);
//        when(mockTemplate.getForEntity(eq(expectedUrl), same(LocationResponse.class))).thenReturn(response);
        when(mockTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), isA(HttpEntity.class), same(LocationResponse.class))).thenReturn(response);

        final LocationRequestCommand command = new LocationRequestCommand("tcap-ss7-ati-enabler", map,  mockTemplate);

//        verify(mockRf).setConnectTimeout(eq(50000));
//        verify(mockRf).setReadTimeout(eq(50000));
        assertThat(command.execute(), sameInstance(response));
    }
}
