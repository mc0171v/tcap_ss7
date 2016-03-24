package com.vennetics.bell.sam.terminal.location.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.vennetics.bell.sam.model.location.LocationResponse;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * A HystrixCommand to invoke Location Request on the internal SS7 service
 */

public class LocationRequestCommand extends HystrixCommand<ResponseEntity<LocationResponse>> {

    private static final Logger LOG = LoggerFactory.getLogger(LocationRequestCommand.class);

    private final RestTemplate template;
    private final String internalServiceName;
    private final MultiValueMap<String, String> map;

    /**
     *
     * @param internalServiceName
     *   The internal service to forward the message to.
     * @param map
     *   The parameters that constitute the location request.
     * @param template
     *   The {@link RestTemplate} to be used to address the service.
     */
    public LocationRequestCommand(final String internalServiceName,
                                  final MultiValueMap<String, String> map,
                                  final RestTemplate template) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(internalServiceName))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("LocationRequest"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                                  .withExecutionTimeoutInMilliseconds(50000)));

        this.internalServiceName = internalServiceName;
        this.map = map;
        this.template = template;
//        SimpleClientHttpRequestFactory rf =
//        	    (SimpleClientHttpRequestFactory) template.getRequestFactory();
//        	rf.setReadTimeout(50000);
//        	rf.setConnectTimeout(50000);
    }

    @Override
    protected ResponseEntity<LocationResponse> run() {
        final String url = buildUrl();
        LOG.debug("LocationRequestCommand {}", url);
		if (url != null) {
//			final ResponseEntity<LocationResponse> response = template.getForEntity(url, LocationResponse.class);
			 final ResponseEntity<LocationResponse> response =
			 template.exchange(buildUrl(), HttpMethod.GET, buildEntity(),
			 LocationResponse.class);
			LOG.debug("LocationRequestCommand result {}", response);
			return response;
		} else {
			return new ResponseEntity<LocationResponse>(HttpStatus.BAD_REQUEST);
		}
    }

    private HttpEntity<MultiValueMap<String, String>> buildEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, headers);
    }

    private String buildUrl() {
    	if (map.getFirst("imsi") != null) {
    		return String.format("http://%s/outbound/location/requests?imsi=%s", internalServiceName, map.getFirst("imsi"));
    	} else if (map.getFirst("msisdn") != null) {
            return String.format("http://%s/outbound/location/requests?msisdn=%s", internalServiceName, map.getFirst("imsi"));
    	} else {
    		return null;
    	}
    }
}
