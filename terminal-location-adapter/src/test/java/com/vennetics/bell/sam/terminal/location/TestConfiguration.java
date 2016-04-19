package com.vennetics.bell.sam.terminal.location;

import com.vennetics.bell.sam.core.CoreErrorsConfig;
import com.vennetics.bell.sam.error.SamErrorsConfig;
import com.vennetics.bell.sam.error.adapters.Ss7ErrorAdapter;
import com.vennetics.bell.sam.error.mapper.IExceptionTypeMapper;
import com.vennetics.bell.sam.model.location.LocationResponse;
import com.vennetics.bell.sam.rest.config.RestConfig;
import com.vennetics.bell.sam.terminal.location.config.LocationAdapterConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import static org.mockito.Mockito.mock;

/**
 * Dummy mainline to define configuration for web integration testing.
 * Replace SmsService bean with a mock. TODO SJ Determine if there is a better way of doing this...
 */

@SpringBootApplication
@ComponentScan(basePackages = { "com.vennetics.bell.sam.terminal.location" },
                excludeFilters = {
                                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = LocationService.class),
                                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = LocationAdapterConfig.class) })
@Import({ SamErrorsConfig.class, RestConfig.class, CoreErrorsConfig.class })
public class TestConfiguration {

    public static final String MSISDN = "tel:+442890269151";
    public static final String MSISDN_KEY = "msisdn";
    public static final String IMSI = "442890269151123";
    public static final String IMSI_KEY = "imsi";
    public static final double LATITUDE = 33.3;
    public static final String LATITUDE_KEY = "latitude";
    public static final double LONGITUDE = 66.6;
    public static final String LONGITUDE_KEY = "longitude";
    public static final double UNCERTAINTY = 1;
    public static final String UNCERTAINTY_KEY = "uncertainty";
    public static final String SERVICE = "tcap-ss7-ati-enabler";

    public static void main(final String[] args) {
        SpringApplication.run(TestConfiguration.class, args);
    }

    @Bean(name = "locationService")
    public ILocationService locationService() {
        return mock(ILocationService.class);
    }

    @Bean
    public IExceptionTypeMapper exceptionTypeMapper() {
        return mock(IExceptionTypeMapper.class);
    }

    @Bean(name = "ss7ErrorAdapter")
    public Ss7ErrorAdapter errorAdapter() {
        return mock(Ss7ErrorAdapter.class);
    }

    public static final MultiValueMap<String, String> createMap() {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add(IMSI_KEY, IMSI);
        return map;
    }

    public static final ResponseEntity<LocationResponse> dummyResponse() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        final LocationResponse response = new LocationResponse();
        response.setLatitude(LATITUDE);
        return new ResponseEntity<LocationResponse>(response, headers, HttpStatus.CREATED);
    }
}
