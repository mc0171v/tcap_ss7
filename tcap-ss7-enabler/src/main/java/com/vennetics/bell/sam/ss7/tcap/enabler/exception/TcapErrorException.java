package com.vennetics.bell.sam.ss7.tcap.enabler.exception;

import com.vennetics.bell.sam.ss7.tcap.enabler.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class TcapErrorException extends SpecificServiceException {

    private static final long serialVersionUID = -7778591932698107919L;

    public TcapErrorException(final String message) {
        super(ServiceErrorMessageType.TCAP_DIALOGUE_EXISTS, message);
    }

}
