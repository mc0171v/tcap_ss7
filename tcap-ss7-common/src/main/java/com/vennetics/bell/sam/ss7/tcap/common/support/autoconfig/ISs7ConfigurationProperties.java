package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;

import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties.Ss7Address;

public interface ISs7ConfigurationProperties {

    String getGsmScfAddress();
    
    void setGsmScfAddress(String gsmScfAddress);
    
    int getStd();
    
    void setStd(final int sTd);
    
    long getInvokeTimeout();
    
    long getLatchTimeout();
    
    void setInvokeTimeout(long invokeTimeout);
    
    void setLatchTimeout(long latchTimeout);
    
    Ss7Address getOrigAddress();
    
    void setOrigAddress(Ss7Address origiAddress);
    
    Ss7Address getDestAddress();
    
    void setDestAddress(Ss7Address destAddress);
}
