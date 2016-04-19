package com.vennetics.bell.sam.ss7.tcap.ati.enabler.service;

import rx.Observable;

import java.util.UUID;

import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;

@FunctionalInterface
public interface IAtiService {

    /***
     * Send an any-time interrogation request to a HLR
     * @param externalRequestId
     *     a UUID external request identifier
     * @param message
     *     An object representing the ATI request
     * @return
     *     An object containing the location and subscriber state if requestwd
     */
    Observable<OutboundATIMessage> sendAtiMessage(UUID externalRequestId,
                                                  OutboundATIMessage message);

}
