package com.vennetics.bell.sam.ss7.tcap.enabler.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IBellSamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.utils.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SendAtiCommand extends HystrixCommand<OutboundATIMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(SendAtiCommand.class);

    private IBellSamTcapEventListener listener;
    private OutboundATIMessage request;

    /**
     * GetSubscriberProfileCommand constructor
     *
     * @param searchFilter
     * @param filterValue
     * @param attributes
     */
    public SendAtiCommand(final IBellSamTcapEventListener listener,
                          final OutboundATIMessage request) {
        super(HystrixCommandGroupKey.Factory.asKey("SS7ATI"));
        this.listener = listener;
        this.request = request;
    }

    /**
     * 
     */
    @Override
    protected OutboundATIMessage run() {
        LOG.debug("Running Hystrix wrapped send ATI command to return a location or status");
        final Callback callback = new Callback();
        listener.startDialogue(request, callback.getListener());
        final MessageWrapper mw = new MessageWrapper();
        callback.getObservable().subscribe(outbound -> { mw.setOutbound(outbound); });
        return mw.getOutbound();
    }
    
    private class MessageWrapper {
        private OutboundATIMessage outbound;

        public OutboundATIMessage getOutbound() {
            return outbound;
        }

        public void setOutbound(final OutboundATIMessage outbound) {
            this.outbound = outbound;
        }
    }
}
