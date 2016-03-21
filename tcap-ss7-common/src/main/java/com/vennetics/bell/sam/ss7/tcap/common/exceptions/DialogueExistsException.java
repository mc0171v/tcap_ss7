package com.vennetics.bell.sam.ss7.tcap.common.exceptions;

import com.vennetics.bell.sam.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class DialogueExistsException extends SpecificServiceException {

    private static final long serialVersionUID = -7778591932698107919L;

    public DialogueExistsException(final Integer id) {
        super(ServiceErrorMessageType.TCAP_DIALOGUE_EXISTS, id.toString());
    }

}
