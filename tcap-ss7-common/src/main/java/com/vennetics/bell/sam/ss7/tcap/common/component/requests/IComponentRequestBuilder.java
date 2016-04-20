package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public interface IComponentRequestBuilder {

    /***
     * Create a TCAP invoke request
     * @param source
     *     the listener source
     * @param invokeId
     *     the invoke identifier
     * @param opData
     *     the operation data
     * @param configProps
     *     the SS7 configuration properties
     * @param dialogueId
     *     the dialogue identifier
     * @return
     *     {@link InvokeReqEvent}
     */
    InvokeReqEvent createInvokeReq(Object source,
                                   int invokeId,
                                   Object opData,
                                   ISs7ConfigurationProperties configProps,
                                   int dialogueId);

    /***
     * Create a TCAP invoke request with parameters
     * @param source
     *     the listener source
     * @param invokeId
     *     the invoke identifier
     * @param opData
     *     the operation data
     * @param withparams
     *     indicate if parameters are included
     * @param configProps
     *     the SS7 configuration properties
     * @param dialogueId
     *     the dialogue identifier
     * @return
     *     {@link InvokeReqEvent}
     */
    InvokeReqEvent createInvokeReq(Object source,
                                   int invokeId,
                                   Object opData,
                                   boolean withparams,
                                   ISs7ConfigurationProperties configProps,
                                   int dialogueId);
    
    /***
     * 
     * @param source
     *     the listener source
     * @param dialogueId
     *     the dialogue identifier
     * @param invokeId
     *     the invoke identifier
     * @return
     *     {@link ResultReqEvent}
     */   
    ResultReqEvent createResultReq(Object source,
                                   int dialogueId,
                                   int invokeId);
}
