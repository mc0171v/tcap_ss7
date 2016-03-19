package com.vennetics.bell.sam.ss7.tcap.enabler.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.listener.ISamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

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
    
    private static final long LATCH_TIMEOUT = 5000; //TODO config

    /**
     * Send an anyTimeInterrogationReq
     * @param listener
     * @param request
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

    /**
     * 
     */
    @Override
    protected OutboundATIMessage run() {
        logger.debug("Running Hystrix wrapped send ATI command to return a location or status");
        final IDialogue dialogue = listener.startDialogue(request, cDl);
        try {
            cDl.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            logger.error("Caught Exception {} waiting for result", ex);
        }
        if (dialogue.getResult() != null) {
            return (OutboundATIMessage) dialogue.getResult();
        }
        return request; //TODO Add an error
    }
}
