package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapComponent;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public abstract class AbstractComponentRequestBuilder implements IComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentRequestBuilder.class);
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder#
     * createInvokeReq(java.lang.Object, int, java.lang.Object, com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties, int)
     */
    @Override
    public abstract InvokeReqEvent createInvokeReq(final TcapComponent tcapComponent);
        
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder#
     * createInvokeReq(java.lang.Object, int, java.lang.Object, com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties, int)
     */
    @Override
    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request,
                                          final ISs7ConfigurationProperties configProps,
                                          final int dialogueId) {
        return createInvokeReq(source,
                               invokeId,
                               request,
                               false,
                               configProps, dialogueId);
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder#
     * createInvokeReq(java.lang.Object, int, java.lang.Object, boolean, com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties, int)
     */
    @Override
    public InvokeReqEvent createInvokeReq(final Object source,
                                          final int invokeId,
                                          final Object request,
                                          final boolean withParams,
                                          final ISs7ConfigurationProperties configProps,
                                          final int dialogueId) {
        logger.error("Unexpected createInvokeReq");
        return null;
    }
   
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder#
     * createResultReq(java.lang.Object, int, int)
     */
    @Override
    public ResultReqEvent createResultReq(final Object source,
                                          final int dialogueId,
                                          final int invokeId) {
        logger.error("Unexpected createResultReq");
        return null;
    }
}
