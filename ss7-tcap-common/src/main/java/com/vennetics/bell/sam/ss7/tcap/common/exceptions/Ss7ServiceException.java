package com.vennetics.bell.sam.ss7.tcap.common.exceptions;

import com.vennetics.bell.sam.error.ServiceErrorMessageType;
import com.vennetics.bell.sam.error.exceptions.SpecificServiceException;

public class Ss7ServiceException extends SpecificServiceException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2675862498148933471L;

    public Ss7ServiceException(final String error) {
        super(ServiceErrorMessageType.SS7_SERVICE_EXCEPTION, error);
    }
}
