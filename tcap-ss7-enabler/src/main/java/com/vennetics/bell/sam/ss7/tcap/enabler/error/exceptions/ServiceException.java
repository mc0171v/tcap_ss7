package com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions;

/**
 * Base class for any exception that will result in the API returning a
 * ServiceException to the client.
 */

public abstract class ServiceException extends BadRequestException {

    private static final long serialVersionUID = 5065966114230563829L;
    protected static final String NON_SPECIFIC_SERVICE_MESSAGE_ID = "SVC0001";

    public ServiceException(final Enum<?> messageCode, final String... messageParams) {
        super(messageCode, messageParams);
    }

    public ServiceException(final Enum<?> messageCode) {
        super(messageCode);
    }

    @Override
    public final String getNonSpecificMessageId() {
        return NON_SPECIFIC_SERVICE_MESSAGE_ID;
    }
}
