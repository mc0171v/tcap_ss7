package com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions;

/**
 * Base class for any exception that will result in the API returning a
 * ServiceException with a specific message id to the client.
 */

public abstract class SpecificServiceException extends ServiceException {

    private static final long serialVersionUID = 5065963214230563829L;

    public SpecificServiceException(final Enum<?> messageCode, final String... messageParams) {
        super(messageCode, messageParams);
    }

    public SpecificServiceException(final Enum<?> messageCode) {
        super(messageCode);
    }

    @Override
    public final boolean isSpecificException() {
        return true;
    }
}
