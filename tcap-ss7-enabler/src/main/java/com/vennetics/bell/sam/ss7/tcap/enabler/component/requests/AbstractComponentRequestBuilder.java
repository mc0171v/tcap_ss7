package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;

public abstract class AbstractComponentRequestBuilder implements IComponentRequestBuilder {

    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request) {
        return createInvokeReq(source, invokeId, request, false);
    }
    
    public abstract InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final Object request,
                                          final boolean withParams);
}
