package com.vennetics.bell.sam.terminal.location.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Creates custom {@link RestTemplate}s, based on Ribbon-aware template.
 */
@Configuration
@ComponentScan(basePackages = { "com.vennetics.bell.sam.terminal.location" })
public class LocationAdapterConfig {

    private static final Logger LOG = LoggerFactory.getLogger(LocationAdapterConfig.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MappingJackson2HttpMessageConverter jsonMessageConverter;

    @Autowired
    private FormHttpMessageConverter formMessageConverter;

    public RestTemplate locationRestTemplate() {
        // Clone the RestTemplate configured by Ribbon and replace the standard message converters with
        // the custom JSON converter.
        LOG.debug("Customising RestTemplate {}", restTemplate);
        final RestTemplate customTemplate = new RestTemplate(restTemplate.getRequestFactory());
        customTemplate.getMessageConverters().clear();
        customTemplate.getMessageConverters().add(jsonMessageConverter);
        customTemplate.getMessageConverters().add(formMessageConverter);
        LOG.debug("Customised RestTemplate {}", customTemplate);
        return customTemplate;
    }
}

