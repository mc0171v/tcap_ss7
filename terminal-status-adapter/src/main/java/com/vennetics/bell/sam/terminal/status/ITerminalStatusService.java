package com.vennetics.bell.sam.terminal.status;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.vennetics.bell.sam.model.status.StatusResponse;


/**
 * Defines an interface to the internal status REST service, for use by the TS adapter.
 */

public interface ITerminalStatusService {
    /**
     * 
     * @param serviceIdentifier
     *   The service to forward the message to.
     * @param params
     *   The parameters that constitute the status request to be sent.
     * @return
     */
    ResponseEntity<StatusResponse> sendStatusRequest(String serviceIdentifier,
                                                     MultiValueMap<String, String> prams);

}
