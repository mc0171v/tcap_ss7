package com.vennetics.bell.sam.terminal.status;

import com.vennetics.bell.sam.model.status.StatusResponse;
import com.vennetics.bell.sam.terminal.status.commands.TerminalStatusRequestCommand;
import com.vennetics.bell.sam.terminal.status.config.TerminalStatusAdapterConfig;

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
 * Default implementation of {@link ITerminalStatusService}.
 */

@Service("terminalStatusService")
@EnableHystrix
public class TerminalStatusService implements ITerminalStatusService, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(TerminalStatusService.class);

    private RestTemplate restTemplate;
    @Autowired
    private TerminalStatusAdapterConfig templateFactory;

    @Override
    public ResponseEntity<StatusResponse> sendStatusRequest(final String serviceIdentifier,
                                                              final MultiValueMap<String, String> params) {
        LOG.debug("Status service:{} params:{}", serviceIdentifier, params);
        return new TerminalStatusRequestCommand(serviceIdentifier,
                                                params,
                                                restTemplate).execute();
    }

    @Override
    @SuppressWarnings({"PMD.SignatureDeclareThrowsException", "squid:S00112"})
    public void afterPropertiesSet() throws Exception {
        restTemplate = templateFactory.statusRestTemplate();
    }
}

