package com.vennetics.bell.sam.terminal.status;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.vennetics.bell.sam.model.subscriber.status.SubscriberStatusResponse;
import com.vennetics.bell.sam.terminal.status.config.TerminalStatusAdapterConfig;


@RunWith(MockitoJUnitRunner.class)
public class TerminalStatusServiceTest {

    @InjectMocks
    @Autowired
    private TerminalStatusService service;

    @Mock private TerminalStatusAdapterConfig mockTemplateFactory;
    @Mock private RestTemplate mockTemplate;
//    @Mock
//    private SimpleClientHttpRequestFactory mockRf;
    
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockTemplateFactory.statusRestTemplate()).thenReturn(mockTemplate);
        service.afterPropertiesSet();
    }

    @Test
    public void shouldSendStatusRequest() throws Exception {
        final String expectedUrl = "http://tcap-ss7-ati-enabler/outbound/status/requests?imsi=442890269151123";
        final ResponseEntity<SubscriberStatusResponse> dummyResponse = TestConfiguration.dummyResponse();
        final MultiValueMap<String, String> map = TestConfiguration.createMap();
        when(mockTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), isA(HttpEntity.class), same(SubscriberStatusResponse.class))).thenReturn(dummyResponse);
        final ResponseEntity<SubscriberStatusResponse> response = service.sendStatusRequest(TestConfiguration.SERVICE, map);
        assertThat(response, is(not(nullValue())));
        assertThat(response, is(sameInstance(dummyResponse)));
    }
}
