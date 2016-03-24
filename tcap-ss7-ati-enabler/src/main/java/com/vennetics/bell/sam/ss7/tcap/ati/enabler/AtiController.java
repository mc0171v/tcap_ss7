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
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.IAtiService;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

import rx.Observable;


@RestController
@RequestMapping("/")
@RefreshScope
@EnableAutoConfiguration
public class AtiController {

    private static final Logger logger = LoggerFactory.getLogger(AtiController.class);
    
//    protected static final String REST_SS7_ATI_URL = ServiceConstants.SS7_ATI_URL + "/tcap-ss7-at-enabler";
//    protected static final String REST_SS7_ATI_URL = "/{serviceIdentifier}";

    @Autowired
    @Qualifier("atiService")
    private IAtiService atiService;


    @RequestMapping(value = "/outbound/location/requests", method = RequestMethod.GET)
    public ResponseEntity<LocationResponse> sendLocationRequest(@RequestParam final MultiValueMap<String, String> params) {
//        public ResponseEntity<LocationResponse> sendLocationRequest(@RequestParam final MultiValueMap<String, String> params,
//                                                                    @PathVariable final String serviceIdentifier) {
        final OutboundATIMessage obm = new OutboundATIMessage();
        obm.setRequestInfoLocation(true);
        final LocationResponse response = new LocationResponse();
        
        if (params.getFirst("msisdn") != null) {
            obm.setMsisdn(params.getFirst("msisdn"));
        } else if (params.getFirst("imsi") != null) {
            obm.setImsi(params.getFirst("imsi"));
        } else {
            new ResponseEntity<LocationResponse>(
                               response,
                               HttpStatus.BAD_REQUEST);
        }
        UUID uuid = UUID.randomUUID();
        Observable<OutboundATIMessage> observer = atiService.sendAtiMessage(uuid, obm);
        observer.subscribe(outboundATIMessage -> { logger.debug("Controller received {}", outboundATIMessage);
                           response.setLatitude(EncodingHelper.bytesToHex(outboundATIMessage.getLatitude()));
                           response.setLongitude(EncodingHelper.bytesToHex(outboundATIMessage.getLongitude()));
                           response.setUncertainty(EncodingHelper.bytesToHex(outboundATIMessage.getUncertainty())); });
        logger.debug("Response {}", response);
        return new ResponseEntity<LocationResponse>(
                        response,
                        HttpStatus.OK);

    }
    
    //TODO change to status
    @RequestMapping(value = "/outbound/status/requests", method = RequestMethod.GET)
    public ResponseEntity<LocationResponse> sendStatusRequest(@RequestParam final MultiValueMap<String, String> params) {
//    public ResponseEntity<LocationResponse> sendStatusRequest(@RequestParam final MultiValueMap<String, String> params,
//                                                              @PathVariable final String serviceIdentifier) {
//        final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("imsi", "12345");
        final OutboundATIMessage obm = new OutboundATIMessage();
        obm.setRequestInfoSubscriberState(true);
        final LocationResponse response = new LocationResponse();
        response.setLatitude("Not Retrieved");
        response.setLongitude("Not Retrieved");
        response.setUncertainty("Not Retrieved");
        if (params.getFirst("msisdn") != null) {
            obm.setMsisdn(params.getFirst("msisdn"));
        } else if (params.getFirst("imsi") != null) {
            obm.setImsi(params.getFirst("imsi"));
        } else {
            new ResponseEntity<LocationResponse>(
                               response,
                               HttpStatus.BAD_REQUEST);
        }
        UUID uuid = UUID.randomUUID();
        Observable<OutboundATIMessage> observer = atiService.sendAtiMessage(uuid, obm);
        observer.subscribe(outboundATIMessage -> { logger.debug("Controller received {}", outboundATIMessage);
                           response.setLatitude(EncodingHelper.bytesToHex(outboundATIMessage.getLatitude()));
                           response.setLongitude(EncodingHelper.bytesToHex(outboundATIMessage.getLongitude()));
                           response.setUncertainty(EncodingHelper.bytesToHex(outboundATIMessage.getUncertainty())); });
        logger.debug("Response {}", response);
        return new ResponseEntity<LocationResponse>(
                        response,
                        HttpStatus.OK);

    }
    
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
