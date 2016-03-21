package com.vennetics.bell.sam.ss7.tcap.common.exceptions;

import com.vennetics.bell.sam.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.error.exceptions.SpecificServiceException;

/**
 * Exception to throw when a supplied address is deemed to be invalid.
 */
public class NoDialogueExistsException extends SpecificServiceException {

    private static final long serialVersionUID = -930347322138664570L;

    public NoDialogueExistsException(final Integer id) {
        super(ServiceErrorMessageType.NO_TCAP_DIALOGUE_EXISTS, id.toString());
    }

}
