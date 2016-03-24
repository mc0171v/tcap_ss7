package com.vennetics.bell.sam.terminal.location;

import com.vennetics.bell.sam.model.location.LocationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;


/**
 * Defines an interface to the internal Location REST service, for use by the TL adapter.
 */

public interface ILocationService {
    /**
     * 
     * @param serviceIdentifier
     *   The service to forward the message to.
     * @param params
     *   The parameters that constitute the location request to be sent.
     * @return
     */
    ResponseEntity<LocationResponse> sendLocationRequest(String serviceIdentifier,
                                                  MultiValueMap<String, String> prams);

}
