package com.vennetics.bell.sam.ss7.tcap.enabler.exception;

import com.vennetics.bell.sam.ss7.tcap.enabler.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class BadProtocolException extends SpecificServiceException {

    private static final long serialVersionUID = 2598998425759025626L;

    public BadProtocolException(final String message) {
        super(ServiceErrorMessageType.BAD_SS7_CONFIG, message);
    }

}
