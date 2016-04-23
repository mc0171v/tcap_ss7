package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapComponent;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public abstract class AbstractTcapComponentRequestBuilder implements ITcapComponentRequestBuilder {
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.component.requests.ITcapComponentRequestBuilder#
     *      createInvokeReq(java.lang.Object, com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapComponent, com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties, int)
     */
    @Override
    public abstract InvokeReqEvent createInvokeReq(final Object source,
                                                   final TcapComponent component,
                                                   final ISs7ConfigurationProperties configProps,
                                                   final int dialogueId);
           
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.component.requests.ITcapComponentRequestBuilder#
     *      createResultReq(com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapComponent)
     */
    @Override
    public abstract ResultReqEvent createResultReq(final TcapComponent tcapComponent);
}
