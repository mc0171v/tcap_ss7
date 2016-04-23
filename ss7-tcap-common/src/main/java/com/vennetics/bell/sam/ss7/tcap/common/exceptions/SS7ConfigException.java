package com.vennetics.bell.sam.ss7.tcap.common.exceptions;

import com.vennetics.bell.sam.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class SS7ConfigException extends SpecificServiceException {

    private static final long serialVersionUID = 2598998425759025626L;

    public SS7ConfigException(final String message) {
        super(ServiceErrorMessageType.BAD_SS7_CONFIG, message);
    }

}
