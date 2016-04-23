package com.vennetics.bell.sam.ss7.tcap.ati.enabler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vennetics.bell.sam.ss7.tcap.ati.enabler.commands.SendAtiCommand;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.common.address.Address;
import com.vennetics.bell.sam.ss7.tcap.common.address.IAddressNormalizer;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.ISamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import rx.Observable;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
public class AtiService implements IAtiService {

    private static final Logger logger = LoggerFactory.getLogger(AtiService.class);
    
    private ISamTcapEventListener listener;

    private IAddressNormalizer addressNormalizer;

    @Autowired
    public AtiService(final ISamTcapEventListener listener,
                      final IAddressNormalizer addressNormalizer) {
        this.listener = listener;
        this.addressNormalizer = addressNormalizer;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.IAtiService#sendAtiMessage(java.util.UUID, com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage)
     */
    @Override
    public Observable<OutboundATIMessage> sendAtiMessage(final UUID externalRequestId,
                                                         final OutboundATIMessage atiMessageRequest) {
        logger.debug(">>> sendAtiMessage({}, {})" + atiMessageRequest,
                     atiMessageRequest.getDestination());
        OutboundATIMessage result = null;
        if (checkAndWaitForListener()) {
            Address normalizedDestination;
            if (null != atiMessageRequest.getMsisdn()) {
                normalizedDestination = normalizeAddress(atiMessageRequest.getMsisdn());
                logger.debug("Normalized address {} to {}", atiMessageRequest.getMsisdn(), normalizedDestination.getE164Address());
                atiMessageRequest.setMsisdn(normalizedDestination.getE164Address().replaceFirst("\\+", ""));
            }
            if (atiMessageRequest.getDestination() != null) {
                final CountDownLatch cDl = new CountDownLatch(1);
                result = new SendAtiCommand(listener,
                                            atiMessageRequest,
                                            cDl).execute();
                logger.debug("ATI Service Constructed ATI Command");
            }
        } else {
            throw new Ss7ServiceException("Not ready for traffic");
        }
        logger.debug("Result = {}", result);
        return Observable.just(result);
    }
    
    public Observable<TcapDialogue> beginDialogue(final TcapDialogue tcapDialogue) {
        return null; 
    }
    
    public Observable<TcapDialogue> continueDialogue(final TcapDialogue tcapDialogue) {
        return null; 
    }
    
    public Observable<TcapDialogue> endDialogue(final TcapDialogue tcapDialogue) {
        return null; 
    }
    
    private Address normalizeAddress(final String suppliedAddress) {
        final Address preNormalizedAddress = new Address();
        preNormalizedAddress.setSuppliedAddress(suppliedAddress);
        return addressNormalizer.normalizeToE164Address(preNormalizedAddress);
    }
    
    private boolean checkAndWaitForListener() {
        if (!checkBound()) {
            return false;
        }
        if (!checkReady()) {
            return false;
        }
        return true;
    }
    
    private boolean checkBound() {
        int retry = 0;
        final ISs7ConfigurationProperties props = listener.getConfigProperties();
        while (!listener.isBound() && retry < props.getWaitForBindRetries()) {
            logger.debug("Waiting for bind {}", retry);
            retry++;
            try {
                Thread.sleep(props.getWaitBeforeBindRetry());
            } catch (Exception ex) {
                logger.debug("Caught interrupted exception{}", ex);
                return false;
            }

        }
        if (!listener.isBound()) {
            logger.debug("Did not bind");
            return false;
        }
        logger.debug("User bound");
        return true;
    }
    
    private boolean checkReady() {
        int retry;
        final ISs7ConfigurationProperties props = listener.getConfigProperties();
        if (props.isWaitForReady()) {
            retry = 0;
            while (!listener.isReady() && retry < props.getWaitForReadyRetries()) {
                logger.debug("Waiting for ready {}", retry);
                retry++;
                try {
                    Thread.sleep(props.getWaitBeforeReadyRetry());
                } catch (Exception ex) {
                    logger.debug("Caught interrupted exception {}", ex);
                    return false;
                }

            }
            if (!listener.isReady()) {
                logger.debug("Is not ready");
                return false;
            }
        }
        logger.debug("User Ready");
        return true;
    }
}
