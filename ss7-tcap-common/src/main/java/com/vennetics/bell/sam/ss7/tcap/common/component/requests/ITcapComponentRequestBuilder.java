package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapComponent;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;

public interface ITcapComponentRequestBuilder {

    /***
     * Create a TCAP ResultReqEvent
     * @param tcapComponent
     * @return
     *     {@link ResultReqEvent}
     */
    ResultReqEvent createResultReq(TcapComponent tcapComponent);

    /***
     * Create a TCAP InvokeReqEvent
     * @param source
     *     the TCAP listener
     * @param component
     *     the TCAP component to be invoked
     * @param configProps
     *     the SS7 configuration properties
     * @param dialogueId
     *     the TCAP dialogue identifier
     * @param isLast
     *     is this the last TCAP component
     * @return
     *     {@link InvokeReqEvent}
     */
    InvokeReqEvent createInvokeReq(Object source,
                                   TcapComponent component,
                                   ISs7ConfigurationProperties configProps,
                                   int dialogueId,
                                   boolean isLast);
}
