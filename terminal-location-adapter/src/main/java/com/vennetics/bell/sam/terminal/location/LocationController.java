package com.vennetics.bell.sam.terminal.location;

import java.util.Arrays;
import java.util.List;

import com.vennetics.bell.sam.model.location.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vennetics.bell.sam.error.adapters.IErrorAdapter;
import com.vennetics.bell.sam.registry.ServiceConstants;
import com.vennetics.bell.sam.rest.controller.PassThruExceptionHandlingRestController;

/**
 * Spring REST Controller for Location Service.
 * This implementation simply forwards to the internal SMS service.
 */

@RestController("locationController")
@RequestMapping(LocationController.LOCATION_URL)
public class LocationController extends PassThruExceptionHandlingRestController {

    private static final Logger LOG = LoggerFactory.getLogger(LocationController.class);

    protected static final String LOCATION_URL = ServiceConstants.LOCATION_URL + "/{serviceIdentifier}/";

    @Autowired
    @Qualifier("locationService")
    private ILocationService locationService;

    @Autowired
    @Qualifier("locationErrorAdapter")
    private IErrorAdapter errorAdapter;

    /**
     * POST endpoint used to create an outgoing location request.
     * @param params
     *   The parameters that make up the location request.
     * @param serviceIdentifier
     *   The identifier of the internal service to use.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<LocationResponse> sendLocationRequest(@RequestParam final MultiValueMap<String, String> params,
                                                                @PathVariable final String serviceIdentifier) {

        LOG.debug("sendLocationRequest: serviceIdentifier:{} params:{}", serviceIdentifier , params);
        final ResponseEntity<LocationResponse> internalResponse = forwardRequest(serviceIdentifier, params);

        LOG.debug("sendLocationRequest response:{}", internalResponse);
        return internalResponse;
    }

    private ResponseEntity<LocationResponse> forwardRequest(final String serviceIdentifier,
                                                           final MultiValueMap<String, String> params) {

        return locationService.sendLocationRequest(serviceIdentifier,
                                                   params);
    }

    @Override
    protected List<Class<?>> exceptionClasses() {
        return Arrays.asList();
    }

    @Override
    protected IErrorAdapter getErrorAdapter() {
        return errorAdapter;
    }
}
