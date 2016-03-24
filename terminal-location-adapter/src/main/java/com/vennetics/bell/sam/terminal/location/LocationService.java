package com.vennetics.bell.sam.terminal.location;

import com.vennetics.bell.sam.model.location.LocationResponse;
import com.vennetics.bell.sam.terminal.location.commands.LocationRequestCommand;
import com.vennetics.bell.sam.terminal.location.config.LocationAdapterConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Default implementation of {@link ILocationService}.
 */

@Service("locationService")
@EnableHystrix
public class LocationService implements ILocationService, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(LocationService.class);

    private RestTemplate restTemplate;
    @Autowired
    private LocationAdapterConfig templateFactory;

    @Override
    public ResponseEntity<LocationResponse> sendLocationRequest(final String serviceIdentifier,
                                                                final MultiValueMap<String, String> params) {
        LOG.debug("Location service:{} params:{}", serviceIdentifier, params);
        return new LocationRequestCommand(serviceIdentifier,
                                  params,
                                  restTemplate).execute();
    }

    @Override
    @SuppressWarnings({"PMD.SignatureDeclareThrowsException", "squid:S00112"})
    public void afterPropertiesSet() throws Exception {
        restTemplate = templateFactory.locationRestTemplate();
    }
}

