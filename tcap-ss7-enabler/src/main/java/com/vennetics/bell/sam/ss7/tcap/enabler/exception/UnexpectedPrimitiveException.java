package com.vennetics.bell.sam.ss7.tcap.enabler.exception;

import com.vennetics.bell.sam.ss7.tcap.enabler.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class UnexpectedPrimitiveException extends SpecificServiceException {

    private static final long serialVersionUID = 3579479264251328615L;

    public UnexpectedPrimitiveException(final Integer id) {
        super(ServiceErrorMessageType.UNEXPECTED_PRIMITIVE, id.toString());
    }

}
