package com.vennetics.bell.sam.ss7.tcap.common.exceptions;

import com.vennetics.bell.sam.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class SamOutOfServiceException extends SpecificServiceException {

    private static final long serialVersionUID = -7778591932698107919L;

    public SamOutOfServiceException(final Integer id) {
        super(ServiceErrorMessageType.SAM_SS7_OOS, id.toString());
    }

}
