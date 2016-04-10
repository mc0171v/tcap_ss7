package com.vennetics.bell.sam.ss7.tcap.ati.sumulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.vennetics.bell.sam.rest.controller.ExceptionHandlingSs7RestController;
import com.vennetics.bell.sam.ss7.tcap.ati.simulator.response.ATIResponseMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.simulator.response.IResponseMgr;
import com.vennetics.bell.sam.ss7.tcap.common.address.Address;
import com.vennetics.bell.sam.ss7.tcap.common.address.IAddressNormalizer;
import com.vennetics.bell.sam.ss7.tcap.common.listener.ISamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.map.SubscriberState;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;


@RestController
@RequestMapping("/")
@RefreshScope
@EnableAutoConfiguration
public class AtiSimController extends ExceptionHandlingSs7RestController {

    private static final Logger logger = LoggerFactory.getLogger(AtiSimController.class);

    @Autowired
    private ISamTcapEventListener listener;
    @Autowired
    private IAddressNormalizer addressNormalizer;
    @Autowired
    private IResponseMgr responseMgr;

    @RequestMapping(value = "/requestinfo/setup", method = RequestMethod.POST)
    public ResponseEntity<Void> setupRequestInfo(@RequestParam final MultiValueMap<String, String> params) {
        logger.debug("setupRequestInfo: {}", params);
        final ATIResponseMessage obm = setupAtiResponse(params);
        if (responseMgr.create(obm)) {
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    
    private ATIResponseMessage setupAtiResponse(final MultiValueMap<String, String> params) {
        final ATIResponseMessage obm = new ATIResponseMessage();
        
        if (params.getFirst("msisdn") != null) {
            final String suppliedAddress = params.getFirst("msisdn");
            final Address normalizedDestination = normalizeAddress(suppliedAddress);
            logger.debug("Normalized address {} to {}", suppliedAddress, normalizedDestination.getE164Address());
            obm.setMsisdn(normalizedDestination.getE164Address().replaceFirst("\\+", ""));
        }
        if (params.getFirst("imsi") != null) {
            obm.setImsi(params.getFirst("imsi"));
        }
        if (params.getFirst("uncertainty") != null) {
            obm.setUncertainty(EncodingHelper.hexTeleStringToByteArray(params.getFirst("uncertainty"))[0]);
        }
        if (params.getFirst("latitude") != null) {
            obm.setLatitude(EncodingHelper.hexTeleStringToByteArray(params.getFirst("latitude")));
        }
        if (params.getFirst("longitude") != null) {
            obm.setLatitude(EncodingHelper.hexTeleStringToByteArray(params.getFirst("longitude")));
        }
        if (params.getFirst("error") != null) {
            obm.setError(params.getFirst("error"));
        }
        if (params.getFirst("status") != null) {
            obm.setStatus(SubscriberState.valueOf(params.getFirst("status")));
        }
        return obm;
    }
    
    private Address normalizeAddress(final String suppliedAddress) {
        final Address preNormalizedAddress = new Address();
        preNormalizedAddress.setSuppliedAddress(suppliedAddress);
        return addressNormalizer.normalizeToE164Address(preNormalizedAddress);
    }
    
    public ISamTcapEventListener getListener() {
        return listener;
    }

    public void setListener(final ISamTcapEventListener listener) {
        this.listener = listener;
    }
    
    public IResponseMgr getResponseMgr() {
        return responseMgr;
    }

    public void setResponseMgr(final IResponseMgr responseMgr) {
        this.responseMgr = responseMgr;
    }
    
    public IAddressNormalizer getAddressNormalizer() {
        return addressNormalizer;
    }

    public void setAddressNormalizer(final IAddressNormalizer addressNormalizer) {
        this.addressNormalizer = addressNormalizer;
    }
}
