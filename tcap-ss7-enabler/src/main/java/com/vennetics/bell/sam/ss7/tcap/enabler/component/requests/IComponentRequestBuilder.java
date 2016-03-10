package com.vennetics.bell.sam.ss7.tcap.enabler.component.requests;

import jain.protocol.ss7.tcap.component.InvokeReqEvent;

public interface IComponentRequestBuilder {

    InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                   final int invokeId,
                                   final Object opData);

    InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                   final int invokeId,
                                   final Object opData,
                                   final boolean withparams);
}
