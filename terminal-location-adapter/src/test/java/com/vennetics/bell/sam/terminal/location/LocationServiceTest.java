package com.vennetics.bell.sam.terminal.location;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.when;


import com.vennetics.bell.sam.model.location.LocationResponse;

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

import com.vennetics.bell.sam.terminal.location.config.LocationAdapterConfig;


@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

    @InjectMocks
    @Autowired
    private LocationService service;

    @Mock private LocationAdapterConfig mockTemplateFactory;
    @Mock private RestTemplate mockTemplate;
    
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockTemplateFactory.locationRestTemplate()).thenReturn(mockTemplate);
        service.afterPropertiesSet();
    }

    @Test
    public void shouldSendLocationRequest() throws Exception {
        final String expectedUrl = "http://tcap-ss7-ati-enabler/outbound/location/requests?imsi=442890269151123";
        final ResponseEntity<LocationResponse> dummyResponse = TestConfiguration.dummyResponse();
        final MultiValueMap<String, String> map = TestConfiguration.createMap();
        when(mockTemplate.exchange(eq(expectedUrl), eq(HttpMethod.GET), isA(HttpEntity.class), same(LocationResponse.class))).thenReturn(dummyResponse);
        final ResponseEntity<LocationResponse> response = service.sendLocationRequest(TestConfiguration.SERVICE, map);
        assertThat(response, is(not(nullValue())));
        assertThat(response, is(sameInstance(dummyResponse)));
    }
}
