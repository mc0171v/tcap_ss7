package com.vennetics.bell.sam.terminal.status;

import java.util.Arrays;
import java.util.List;

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
import com.vennetics.bell.sam.model.status.StatusResponse;
import com.vennetics.bell.sam.registry.ServiceConstants;
import com.vennetics.bell.sam.rest.controller.PassThruExceptionHandlingRestController;

/**
 * Spring REST Controller for Status Service.
 * This implementation simply forwards to the internal SS7 application.
 */

@RestController("statusController")
@RequestMapping(TerminalStatusController.STATUS_URL)
public class TerminalStatusController extends PassThruExceptionHandlingRestController {

    private static final Logger LOG = LoggerFactory.getLogger(TerminalStatusController.class);

    protected static final String STATUS_URL = ServiceConstants.STATUS_URL + "/{serviceIdentifier}/";

    @Autowired
    @Qualifier("terminalStatusService")
    private ITerminalStatusService terminalStatusService;

    @Autowired
    @Qualifier("ss7ErrorAdapter")
    private IErrorAdapter errorAdapter;

    /**
     * GET endpoint used to create an outgoing status request.
     * @param params
     *   The parameters that make up the status request.
     * @param serviceIdentifier
     *   The identifier of the internal service to use.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<StatusResponse> sendStatusRequest(@RequestParam final MultiValueMap<String, String> params,
                                                            @PathVariable final String serviceIdentifier) {

        LOG.debug("sendStatusRequest: serviceIdentifier:{} params:{}", serviceIdentifier , params);
        final ResponseEntity<StatusResponse> internalResponse = forwardRequest(serviceIdentifier, params);

        LOG.debug("sendStatusRequest response:{}", internalResponse);
        return internalResponse;
    }

    private ResponseEntity<StatusResponse> forwardRequest(final String serviceIdentifier,
                                                          final MultiValueMap<String, String> params) {

        return terminalStatusService.sendStatusRequest(serviceIdentifier,
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
