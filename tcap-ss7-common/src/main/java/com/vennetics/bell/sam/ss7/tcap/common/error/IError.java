package com.vennetics.bell.sam.ss7.tcap.common.error;

import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;

public interface IError {

    void setError(Ss7ServiceException error);
}
