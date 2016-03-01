package com.vennetics.bell.sam.ss7.tcap.enabler.error.exceptions;

/**
 * Extends {@link AbstractFormattedException} with messageType, message
 * arguments and timestamp.
 */

public abstract class ApiException extends AbstractFormattedException {

    private static final long serialVersionUID = 5065966114610563829L;

    private final String[] messageArguments;

    private final Enum<?> errorMessageType;

    private final long timestamp = System.currentTimeMillis();

    public ApiException(final Enum<?> messageCode) {
        super(messageCode.toString());
        errorMessageType = messageCode;
        messageArguments = new String[0];
    }

    public ApiException(final Enum<?> messageCode, final String... messageParams) {
        super(messageCode.toString() + toString(messageParams));
        errorMessageType = messageCode;
        messageArguments = messageParams;
    }

    public ApiException(final Throwable e, final Enum<?> messageCode) {
        super(e, messageCode.toString());
        errorMessageType = messageCode;
        messageArguments = new String[0];
    }

    public ApiException(final Throwable e,
                        final Enum<?> messageCode,
                        final String... messageParams) {
        super(e, messageCode.toString() + toString(messageParams));
        errorMessageType = messageCode;
        messageArguments = messageParams;
    }

    public Enum getErrorMessageType() {
        return errorMessageType;
    }

    public String[] getMessageArguments() {
        return messageArguments != null ? messageArguments.clone() : new String[0];
    }

    public long getTimestamp() {
        return timestamp;
    }

    private static String toString(final String... messageParams) {
        return messageParams[0];
    }
}
