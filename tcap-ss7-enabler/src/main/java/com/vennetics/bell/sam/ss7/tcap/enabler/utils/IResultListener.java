package com.vennetics.bell.sam.ss7.tcap.enabler.utils;

import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

public interface IResultListener {

    void handleEvent(OutboundATIMessage message);
                    
}
