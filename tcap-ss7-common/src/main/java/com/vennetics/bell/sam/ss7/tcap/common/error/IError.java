package com.vennetics.bell.sam.ss7.tcap.common.error;

import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;

public interface IError {

    /***
     * Set an error {@link Ss7ServiceException} on an object
     * @param error
     */
    void setError(Ss7ServiceException error);
}
