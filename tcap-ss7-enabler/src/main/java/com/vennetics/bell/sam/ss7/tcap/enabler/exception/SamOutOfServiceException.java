package com.vennetics.bell.sam.ss7.tcap.enabler.exception;

import com.vennetics.bell.sam.ss7.tcap.enabler.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class SamOutOfServiceException extends SpecificServiceException {

    private static final long serialVersionUID = -7778591932698107919L;

    public SamOutOfServiceException(final Integer id) {
        super(ServiceErrorMessageType.TCAP_DIALOGUE_EXISTS, id.toString());
    }

}
