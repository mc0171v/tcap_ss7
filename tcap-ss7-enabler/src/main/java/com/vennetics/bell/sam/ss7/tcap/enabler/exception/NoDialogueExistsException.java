package com.vennetics.bell.sam.ss7.tcap.enabler.exception;

import com.vennetics.bell.sam.ss7.tcap.enabler.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class NoDialogueExistsException extends SpecificServiceException {

    private static final long serialVersionUID = -930347322138664570L;

    public NoDialogueExistsException(final Integer id) {
        super(ServiceErrorMessageType.NO_TCAP_DIALOGUE_EXISTS, id.toString());
    }

}