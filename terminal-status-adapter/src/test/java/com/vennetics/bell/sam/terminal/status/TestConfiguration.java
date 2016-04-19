package com.vennetics.bell.sam.terminal.status;

import com.vennetics.bell.sam.core.CoreErrorsConfig;
import com.vennetics.bell.sam.error.SamErrorsConfig;
import com.vennetics.bell.sam.error.adapters.Ss7ErrorAdapter;
import com.vennetics.bell.sam.error.mapper.IExceptionTypeMapper;
import com.vennetics.bell.sam.model.subscriber.status.SubscriberStatusResponse;
import com.vennetics.bell.sam.rest.config.RestConfig;
import com.vennetics.bell.sam.terminal.status.config.TerminalStatusAdapterConfig;

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
@ComponentScan(basePackages = { "com.vennetics.bell.sam.terminal.status" },
                excludeFilters = {
                                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TerminalStatusService.class),
                                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TerminalStatusAdapterConfig.class) })
@Import({ SamErrorsConfig.class, RestConfig.class, CoreErrorsConfig.class })
public class TestConfiguration {

    public static final String MSISDN = "tel:+442890269151";
    public static final String MSISDN_KEY = "msisdn";
    public static final String IMSI = "442890269151123";
    public static final String IMSI_KEY = "imsi";
    public static final String SERVICE = "tcap-ss7-ati-enabler";

    public static void main(final String[] args) {
        SpringApplication.run(TestConfiguration.class, args);
    }

    @Bean(name = "terminalStatusService")
    public ITerminalStatusService terminalStatusService() {
        return mock(ITerminalStatusService.class);
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

    public static final ResponseEntity<SubscriberStatusResponse> dummyResponse() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        final SubscriberStatusResponse response = new SubscriberStatusResponse();
        response.setSubscriberStatus("Busy");
        return new ResponseEntity<SubscriberStatusResponse>(response, headers, HttpStatus.CREATED);
    }
}
