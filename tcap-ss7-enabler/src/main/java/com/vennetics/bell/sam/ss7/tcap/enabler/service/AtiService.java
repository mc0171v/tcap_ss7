package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vennetics.bell.sam.ss7.tcap.enabler.address.Address;
import com.vennetics.bell.sam.ss7.tcap.enabler.address.IAddressNormalizer;
import com.vennetics.bell.sam.ss7.tcap.enabler.commands.SendAtiCommand;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import rx.Observable;

import java.util.UUID;

@Service
public class AtiService implements IAtiService {

    private static final Logger logger = LoggerFactory.getLogger(AtiService.class);

    private IBellSamTcapEventListener listener;

    private IAddressNormalizer addressNormalizer;

    @Autowired
    public AtiService(final IBellSamTcapEventListener smppAdapter,
                      final IAddressNormalizer addressNormalizer) {
        this.listener = smppAdapter;
        this.addressNormalizer = addressNormalizer;
    }

    @Override
    public Observable<OutboundATIMessage> sendAtiMessage(final UUID externalRequestId,
                                                         final OutboundATIMessage atiMessageRequest) {
        logger.debug(">>> sendAtiMessage({}, {})" + atiMessageRequest,
                     atiMessageRequest.getDestination());
        OutboundATIMessage result = null;
        if (checkAndWaitForListener()) {
            Address normalizedDestination = null;
            if (null != atiMessageRequest.getMsisdn()) {
                normalizedDestination = normalizeAddress(atiMessageRequest.getMsisdn());
                atiMessageRequest.setMsisdn(normalizedDestination.getE164Address());
            }
            if (atiMessageRequest.getDestination() != null) {
                 result = new SendAtiCommand(listener,
                                             atiMessageRequest).execute();
                logger.debug("ATI Service Constructed ATI Command");
            }
        } else {
            atiMessageRequest.setStatus(99);
            return Observable.just(atiMessageRequest); //Unknown
        }
        return Observable.just(result);
    }
    
    private Address normalizeAddress(final String suppliedAddress) {
        final Address preNormalizedAddress = new Address();
        preNormalizedAddress.setSuppliedAddress(suppliedAddress);
        return addressNormalizer.normalizeToE164Address(preNormalizedAddress);
    }
    
    private boolean checkAndWaitForListener() {
        int retry = 0;
        while (!listener.isBound() && retry < 6) {
            logger.debug("Waiting for bind {}", retry);
            retry++;
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                logger.debug("Caught exception");
            }

        }
        if (!listener.isBound()) {
            logger.debug("Did not bind");
            return false;
        }
        logger.debug("User bound");
        retry = 0;
        while (!listener.isReady() && retry < 6) {
            logger.debug("Waiting for ready {}", retry);
            retry++;
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                logger.debug("Caught exception");
            }

        }
        if (!listener.isReady()) {
            logger.debug("Is not ready");
            return false;
        }
        logger.debug("User Ready");
        return true;
    }
}
