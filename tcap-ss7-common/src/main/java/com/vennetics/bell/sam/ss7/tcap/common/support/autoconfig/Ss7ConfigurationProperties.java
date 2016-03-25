package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ericsson.jain.protocol.ss7.tcap.Tools;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

@ConfigurationProperties(prefix = "ss7")
public class Ss7ConfigurationProperties implements ISs7ConfigurationProperties {

    private Ss7Address origAddress;
    private Ss7Address destAddress;
    private String gsmScfAddress;
    private int std;
    private long invokeTimeout;
    private long latchTimeout;  
    private int waitForBindRetries;
    private int waitForReadyRetries;
    private long waitBeforeBindRetry;
    private long waitBeforeReadyRetry;
    private boolean waitForReady;
    private String cpConfig;

    public Ss7ConfigurationProperties() {
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
    
    public int getStd() {
        return std;
    }

    public void setStd(final int sTD) {
        this.std = sTD;
    }

    public String getGsmScfAddress() {
        return gsmScfAddress;
    }

    public void setGsmScfAddress(final String gsmScfAddress) {
        this.gsmScfAddress = gsmScfAddress;
    }
    
    public int getsTd() {
        return std;
    }

    public void setsTd(final int sTd) {
        this.std = sTd;
    }

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(final long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }
    
    public long getLatchTimeout() {
        return latchTimeout;
    }

    public void setLatchTimeout(final long latchTimeout) {
        this.latchTimeout = latchTimeout;
    }
    
    public int getWaitForBindRetries() {
        return waitForBindRetries;
    }

    public void setWaitForBindRetries(final int waitForBindRetries) {
        this.waitForBindRetries = waitForBindRetries;
    }

    public int getWaitForReadyRetries() {
        return waitForReadyRetries;
    }

    public void setWaitForReadyRetries(final int waitForReadyRetries) {
        this.waitForReadyRetries = waitForReadyRetries;
    }

    public long getWaitBeforeBindRetry() {
        return waitBeforeBindRetry;
    }

    public void setWaitBeforeBindRetry(final long waitBeforeBindRetry) {
        this.waitBeforeBindRetry = waitBeforeBindRetry;
    }

    public long getWaitBeforeReadyRetry() {
        return waitBeforeReadyRetry;
    }

    public void setWaitBeforeReadyRetry(final long waitBeforeReadyRetry) {
        this.waitBeforeReadyRetry = waitBeforeReadyRetry;
    }

    public boolean isWaitForReady() {
        return waitForReady;
    }

    public void setWaitForReady(final boolean waitForReady) {
        this.waitForReady = waitForReady;
    } 

    public String getCpConfig() {
        return cpConfig;
    }

    public void setCpConfig(final String cpConfig) {
        this.cpConfig = cpConfig;
    }

    
    public String toString() {
        return "Orig Address: " + origAddress.toString()
             + " Dest Address: " + destAddress.toString()
             + " GSM SCF Address: " + gsmScfAddress
             + " Invoke Timeout: " + invokeTimeout
             + " Latch Timeout: " + invokeTimeout
             + " STD: " + std
             + " waitForBindRetries: " + waitForBindRetries
             + " waitForReadyRetries: " + waitForReadyRetries
             + " waitBeforeBindRetry: " + waitBeforeBindRetry
             + " waitBeforeReadyRetry: " + waitBeforeReadyRetry
             + " waitForReady: " + waitForReady
             + " cpConfig: " + cpConfig;
    }
    
    public static class Ss7Address {
        
        private short ssn;
        private byte[] spc;
        
        public short getSsn() {
            return ssn;
        }

        public void setSsn(final short ssn) {
            this.ssn = ssn;
        }

        public byte[] getSpc() {
            return spc;
        }

        public void setSpc(long spc) {
            this.spc = convertToByte(spc);
        }
        
        private byte[] convertToByte(long spc) {
            final byte[] spcAsBytes =  { Tools.getLoByteOf4(spc), Tools.getNextToLoByteOf4(spc), Tools.getNextToHiByteOf4(spc)};
            return spcAsBytes;
        }
        
        @Override
        public String toString() {
            return "SSN: " + ssn + " SPC: " + EncodingHelper.bytesToHex(spc);
        }

    }
}
