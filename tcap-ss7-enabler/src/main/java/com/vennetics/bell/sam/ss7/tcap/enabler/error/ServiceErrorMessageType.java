package com.vennetics.bell.sam.ss7.tcap.enabler.error;

import com.vennetics.bell.sam.core.errors.ErrorTypeSet;

/**
 * Core Keys into the message properties file for localising error messages.
 * Each value represents a unique error message and should be used within a
 * common service exception.
 */
@ErrorTypeSet("/common-service-error-messages.yml")
public enum ServiceErrorMessageType {

                                     NO_VALID_ADDRESS_ERROR,
                                     INVALID_INPUT_VALUE,
                                     TCAP_DIALOGUE_EXISTS,
                                     NO_TCAP_DIALOGUE_EXISTS,
                                     UNEXPECTED_PRIMITIVE,
                                     TCAP_ERROR_EXCEPTION,
                                     BAD_SS7_CONFIG,
                                     BAD_SS7_SAP;

}
