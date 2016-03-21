package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;

@ConfigurationProperties(prefix = "ss7")
public class Ss7ConfigurationProperties implements ISs7ConfigurationProperties {

    private Ss7Address origAddress;
    private Ss7Address destAddress;
    private String gsmScfAddress;
    private int sTd;
    private long invokeTimeout;

    public Ss7ConfigurationProperties() {
        final byte[] spcA = {
                Tools.getLoByteOf2(231), // zone
                3,
                0 };
        this.origAddress = new Ss7Address(spcA, (short) 99);
        this.destAddress = new Ss7Address(spcA, (short) 98);
        this.sTd = DialogueConstants.PROTOCOL_VERSION_ITU_97;
        this.gsmScfAddress = "12344321";
        this.invokeTimeout = 10000;
    }
    
    public Ss7Address getOrigAddress() {
        return origAddress;
    }

    public void setOrigAddress(final Ss7Address origiAddress) {
        this.origAddress = origiAddress;
    }

    public Ss7Address getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(final Ss7Address destAddress) {
        this.destAddress = destAddress;
    }
    
    public int getsTD() {
        return sTd;
    }

    public void setsTD(final int sTD) {
        this.sTd = sTD;
    }

    public String getGsmScfAddress() {
        return gsmScfAddress;
    }

    public void setGsmScfAddress(final String gsmScfAddress) {
        this.gsmScfAddress = gsmScfAddress;
    }
    
    public int getsTd() {
        return sTd;
    }

    public void setsTd(final int sTd) {
        this.sTd = sTd;
    }

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(final long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }
}
