package com.vennetics.bell.sam.ss7.tcap.ati.enabler.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.ISamTcapEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SendAtiCommand extends HystrixCommand<OutboundATIMessage> {

    private static final Logger logger = LoggerFactory.getLogger(SendAtiCommand.class);

    private ISamTcapEventListener listener;
    private OutboundATIMessage request;
    private CountDownLatch cDl;
    
    /**
     * Send an anyTimeInterrogationReq
     * @param listener
     * @param request
     * @param cDl
     */
    public SendAtiCommand(final ISamTcapEventListener listener,
                          final OutboundATIMessage request,
                          final CountDownLatch cDl) {
        super(HystrixCommandGroupKey.Factory.asKey("SS7ATI"), 10000);
        this.listener = listener;
        this.request = request;
        this.cDl = cDl;
        logger.debug("Constructed ATI Command");
    }

    @Override
    protected OutboundATIMessage run() {
        logger.debug("Running Hystrix wrapped send ATI command to return a location or status");
        final IDialogue dialogue = listener.startDialogue(request, cDl);
        try {
            cDl.await(listener.getConfigProperties().getLatchTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            final String errorMessage = "Caught InterruptedException waiting for result";
            logger.error(errorMessage);
            throw new Ss7ServiceException(errorMessage);
        }
        logger.debug("Dialogue returned with result {}", dialogue.getResult());
        if (dialogue.getResult() != null) {
            OutboundATIMessage obm = (OutboundATIMessage) dialogue.getResult();
            if (obm.getError() == null) {
                logger.debug("Dialogue returned with error {}", obm.getError());
                return obm;
            }
            throw obm.getError();
        }
        throw new Ss7ServiceException("No result");
    }
}
