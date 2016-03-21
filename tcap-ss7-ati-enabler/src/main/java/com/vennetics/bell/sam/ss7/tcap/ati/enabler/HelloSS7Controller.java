package com.vennetics.bell.sam.ss7.tcap.ati.enabler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vennetics.bell.sam.ss7.tcap.ati.enabler.common.ServiceConstants;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.IAtiService;

import rx.Observable;

//import rx.Observable;


@RestController
//@RequestMapping(HelloSS7Controller.REST_SS7_ATI_URL)
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
        logger.debug("Hello SS7");
        OutboundATIMessage obm = new OutboundATIMessage();
        obm.setImsi("12345678");
        obm.setRequestInfoSubscriberState(true);
        UUID uuid = UUID.randomUUID();
        Observable<OutboundATIMessage> observer = atiService.sendAtiMessage(uuid, obm);
        observer.subscribe(outboundATIMessage -> { logger.debug("Controller received {}", outboundATIMessage); });
        return "Started Dialogue";

    }

    public void setAtiService(final IAtiService atiService) {
        this.atiService = atiService;
    }
}
