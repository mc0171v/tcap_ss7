package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;

public interface ISs7ConfigurationProperties {

    String getGsmScfAddress();
    
    void setGsmScfAddress(String gsmScfAddress);
    
    int getsTd();
    
    void setsTd(final int sTd);
    
    long getInvokeTimeout();
    
    void setInvokeTimeout(long invokeTimeout);
    
    Ss7Address getOrigAddress();
    
    void setOrigAddress(Ss7Address origiAddress);
    
    Ss7Address getDestAddress();
    
    void setDestAddress(Ss7Address destAddress);
    
    int getsTD();
    
    void setsTD(final int sTD);
}
