package com.vennetics.bell.sam.ss7.tcap.ati.enabler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vennetics.bell.sam.model.location.LocationResponse;
import com.vennetics.bell.sam.model.subscriber.status.SubscriberStatusResponse;
import com.vennetics.bell.sam.rest.controller.ExceptionHandlingSs7RestController;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.IAtiService;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

import rx.Observable;


@RestController
@RequestMapping("/")
@RefreshScope
@EnableAutoConfiguration
public class AtiController extends ExceptionHandlingSs7RestController {

    private static final Logger logger = LoggerFactory.getLogger(AtiController.class);
    
    @Autowired
    @Qualifier("atiService")
    private IAtiService atiService;


    @RequestMapping(value = "/outbound/location/requests", method = RequestMethod.GET)
    public ResponseEntity<LocationResponse> sendLocationRequest(@RequestParam final MultiValueMap<String, String> params) {
        final OutboundATIMessage obm = setupOutBoundMessage(params);
        final LocationResponse response = new LocationResponse();
        obm.setRequestInfoLocation(true);
        UUID uuid = UUID.randomUUID();
        Observable<OutboundATIMessage> observer = atiService.sendAtiMessage(uuid, obm);
        observer.subscribe(outboundATIMessage -> { logger.debug("Controller received {}", outboundATIMessage);
                           response.setLatitude(EncodingHelper.bytesToHex(outboundATIMessage.getLatitude()));
                           response.setLongitude(EncodingHelper.bytesToHex(outboundATIMessage.getLongitude()));
                           response.setUncertainty(EncodingHelper.bytesToHex(outboundATIMessage.getUncertainty()));
                           });
        logger.debug("Response {}", response);
        return new ResponseEntity<LocationResponse>(
                        response,
                        HttpStatus.OK);

    }
    
    @RequestMapping(value = "/outbound/status/requests", method = RequestMethod.GET)
    public ResponseEntity<SubscriberStatusResponse> sendStatusRequest(@RequestParam final MultiValueMap<String, String> params) {
        final OutboundATIMessage obm = setupOutBoundMessage(params);
        final SubscriberStatusResponse response = new SubscriberStatusResponse();
        obm.setRequestInfoSubscriberState(true);
        UUID uuid = UUID.randomUUID();
        Observable<OutboundATIMessage> observer = atiService.sendAtiMessage(uuid, obm);
        observer.subscribe(outboundATIMessage -> { logger.debug("Controller received {}", outboundATIMessage);
                           response.setSubscriberStatus(outboundATIMessage.getStatus().name()); 
                           });
        logger.debug("Response {}", response);
        return new ResponseEntity<SubscriberStatusResponse>(
                        response,
                        HttpStatus.OK);

    }

    public void setAtiService(final IAtiService atiService) {
        this.atiService = atiService;
    }
    
    private static OutboundATIMessage setupOutBoundMessage(final MultiValueMap<String, String> params) {
        final OutboundATIMessage obm = new OutboundATIMessage();
        
        if (params.getFirst("msisdn") != null) {
            obm.setMsisdn(params.getFirst("msisdn"));
        } else if (params.getFirst("imsi") != null) {
            obm.setImsi(params.getFirst("imsi"));
        } else {
            throw new Ss7ServiceException("One paramater msisdn or imsi must be supplied");
        }
        return obm;
    }
}
