package com.vennetics.bell.sam.terminal.status.commands;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;

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

import com.vennetics.bell.sam.model.subscriber.status.SubscriberStatusResponse;
import com.vennetics.bell.sam.terminal.status.TestConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class TerminalStatusRequestCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(TerminalStatusRequestCommandTest.class);

    @Mock
    private RestTemplate mockTemplate;

    @Test
    public void shouldSendStatusRequest() throws Exception {
        final String expectedUrl = String.format("http://tcap-ss7-ati-enabler/outbound/status/requests?imsi=%s", TestConfiguration.IMSI);
        LOG.debug(expectedUrl);
        MultiValueMap<String, String> map = TestConfiguration.createMap();
        ResponseEntity<SubscriberStatusResponse> response = TestConfiguration.dummyResponse();
        when(mockTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), isA(HttpEntity.class), same(SubscriberStatusResponse.class))).thenReturn(response);

        final TerminalStatusRequestCommand command = new TerminalStatusRequestCommand("tcap-ss7-ati-enabler", map,  mockTemplate);

        assertThat(command.execute(), sameInstance(response));
    }
}
