package com.vennetics.bell.sam.ss7.tcap.ati.simulator.service;

import rx.Observable;

import java.util.UUID;

import com.vennetics.bell.sam.ss7.tcap.ati.simulator.rest.OutboundATIMessage;

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
