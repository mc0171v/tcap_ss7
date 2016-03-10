package com.vennetics.bell.sam.ss7.tcap.enabler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.vennetics.bell.sam.ss7.tcap.enabler.common.ServiceConstants;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IAtiService;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IBellSamTcapEventListener;

import generated.oma.xml.rest.netapi.sms._1.OutboundSMSMessageRequest;
import rx.Observable;


@RestController
@RequestMapping(HelloSS7Controller.REST_SS7_ATI_URL)
@RefreshScope
@EnableAutoConfiguration
public class HelloSS7Controller {

    private static final Logger logger = LoggerFactory.getLogger(HelloSS7Controller.class);
    
    protected static final String REST_SS7_ATI_URL = ServiceConstants.SS7_ATI_URL + "/{serviceIdentifier}";
    
    @Autowired
    @Qualifier("atiService")
    private IAtiService atiService;


    @RequestMapping("/helloss7")
    public String message() {
        return "Started Dialogue";

    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/outbound/requests")
    public  DeferredResult<ResponseEntity<OutboundATIMessage>> ati(@PathVariable final String serviceIdentifier,
                      @RequestBody final OutboundATIMessage message) {

        final UUID externalRequestId = UUID.randomUUID();
        final Observable<OutboundATIMessage> observable = atiService.sendAtiMessage(externalRequestId, message);
        final DeferredResult<ResponseEntity<OutboundATIMessage>> deferred = new DeferredResult<>();
        observable.subscribe(outBoundMessage -> {
            deferred.setResult(new ResponseEntity<OutboundATIMessage>(outBoundMessage,
                                                                      HttpStatus.CREATED));
        });
        return deferred;
    }

    public void setAtiService(final IAtiService atiService) {
        this.atiService = atiService;
    }
}
