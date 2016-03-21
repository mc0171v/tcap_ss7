package com.vennetics.bell.sam.ss7.tcap.ati.enabler.service;

import rx.Observable;

import java.util.UUID;

import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;

@FunctionalInterface
public interface IAtiService {

    /**
     * 
     * @param externalRequestId
     * @param senderAddress
     * @param message
     * @return
     */
    Observable<OutboundATIMessage> sendAtiMessage(UUID externalRequestId,
                                                  OutboundATIMessage message);

}
