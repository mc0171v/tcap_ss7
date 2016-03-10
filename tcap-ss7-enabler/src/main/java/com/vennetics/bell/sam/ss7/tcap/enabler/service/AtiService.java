package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vennetics.bell.sam.ss7.tcap.enabler.address.Address;
import com.vennetics.bell.sam.ss7.tcap.enabler.address.IAddressNormalizer;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

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
        Address normalizedDestination = null;
        if (null != atiMessageRequest.getMsisdn()) {
            normalizedDestination = normalizeAddress(atiMessageRequest.getMsisdn());
        }
        if (atiMessageRequest.getDestination() != null) {
            final Observable<OutboundATIMessage> observable = processAtiRequest(externalRequestId,
                                                                                  atiMessageRequest,
                                                                                  normalizedDestination);
            observable.subscribe(new SendAtiResultsHandler(externalRequestId, atiMessageRequest));

        }
        return Observable.just(atiMessageRequest);
    }

    private Observable<OutboundATIMessage> processAtiRequest(final UUID externalRequestId,
                                                             final OutboundATIMessage message,
                                                             final Address destinationAddresses) {
            final Observable<IDialogue> observable = Observable.from(listener.startDialogue(message));
                                                                                                                  
        return observable;
    }

    private Address normalizeAddress(final String suppliedAddress) {
        final Address preNormalizedAddress = new Address();
        preNormalizedAddress.setSuppliedAddress(suppliedAddress);
        return addressNormalizer.normalizeToE164Address(preNormalizedAddress);
    }

    private final class SendAtiResultsHandler extends Subscriber<OutboundATIMessage> {
        private UUID externalSequenceId;
        private OutboundATIMessage smsMessageRequest;

        private SendAtiResultsHandler(final UUID externalSequenceId,
                                      final OutboundATIMessage smsMessageRequest) {
            this.externalSequenceId = externalSequenceId;
            this.smsMessageRequest = smsMessageRequest;
        }

        @Override
        public void onNext(final OutboundATIMessage item) {
            if (item != null && StringUtils.isNotEmpty(item.getDestination())) {
            }
        }

        @Override
        public void onError(final Throwable error) {
            logger.error("Error encountered while attempting to send SMS.", error);
        }

        @Override
        public void onCompleted() {
            logger.debug("Sequence complete.");
        }
    }

    private final class SendSmsErrorHandler implements
                                            Func1<Throwable, Observable<OutboundATIMessage>> {
        private UUID externalSequenceId;
        private OutboundATIMessage smsMessageRequest;
        private String destinationAddress;

        private SendSmsErrorHandler(final UUID externalSequenceId,
                                    final OutboundATIMessage smsMessageRequest,
                                    final String destinationAddress) {
            this.externalSequenceId = externalSequenceId;
            this.smsMessageRequest = smsMessageRequest;
            this.destinationAddress = destinationAddress;
        }

        @Override
        public Observable<OutboundATIMessage> call(final Throwable e) {
            logger.error("Exception received: {}/{}", e.getMessage(), e);
            return Observable.just(new OutboundATIMessage());
        }
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
