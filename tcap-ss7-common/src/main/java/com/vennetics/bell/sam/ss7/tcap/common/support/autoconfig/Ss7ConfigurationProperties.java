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
    
    @Override
    public Ss7Address getOrigAddress() {
        return origAddress;
    }

    @Override
    public void setOrigAddress(final Ss7Address origiAddress) {
        this.origAddress = origiAddress;
    }

    @Override
    public Ss7Address getDestAddress() {
        return destAddress;
    }

    @Override
    public void setDestAddress(final Ss7Address destAddress) {
        this.destAddress = destAddress;
    }

    @Override
    public int getStd() {
        return std;
    }

    @Override
    public void setStd(final int sTD) {
        this.std = sTD;
    }

    @Override
    public String getGsmScfAddress() {
        return gsmScfAddress;
    }

    @Override
    public void setGsmScfAddress(final String gsmScfAddress) {
        this.gsmScfAddress = gsmScfAddress;
    }

    @Override
    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    @Override
    public void setInvokeTimeout(final long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }
    
    @Override
    public long getLatchTimeout() {
        return latchTimeout;
    }

    @Override
    public void setLatchTimeout(final long latchTimeout) {
        this.latchTimeout = latchTimeout;
    }
    
    @Override
    public int getWaitForBindRetries() {
        return waitForBindRetries;
    }

    @Override
    public void setWaitForBindRetries(final int waitForBindRetries) {
        this.waitForBindRetries = waitForBindRetries;
    }

    @Override
    public int getWaitForReadyRetries() {
        return waitForReadyRetries;
    }

    @Override
    public void setWaitForReadyRetries(final int waitForReadyRetries) {
        this.waitForReadyRetries = waitForReadyRetries;
    }

    @Override
    public long getWaitBeforeBindRetry() {
        return waitBeforeBindRetry;
    }

    @Override
    public void setWaitBeforeBindRetry(final long waitBeforeBindRetry) {
        this.waitBeforeBindRetry = waitBeforeBindRetry;
    }

    @Override
    public long getWaitBeforeReadyRetry() {
        return waitBeforeReadyRetry;
    }

    @Override
    public void setWaitBeforeReadyRetry(final long waitBeforeReadyRetry) {
        this.waitBeforeReadyRetry = waitBeforeReadyRetry;
    }

    @Override
    public boolean isWaitForReady() {
        return waitForReady;
    }

    @Override
    public void setWaitForReady(final boolean waitForReady) {
        this.waitForReady = waitForReady;
    }

    @Override
    public String getCpConfig() {
        return cpConfig;
    }

    @Override
    public void setCpConfig(final String cpConfig) {
        this.cpConfig = cpConfig;
    }

    @Override
    public String toString() {
             String output = " GSM SCF Address: " + gsmScfAddress
             + " Invoke Timeout: " + invokeTimeout
             + " Latch Timeout: " + invokeTimeout
             + " STD: " + std
             + " waitForBindRetries: " + waitForBindRetries
             + " waitForReadyRetries: " + waitForReadyRetries
             + " waitBeforeBindRetry: " + waitBeforeBindRetry
             + " waitBeforeReadyRetry: " + waitBeforeReadyRetry
             + " waitForReady: " + waitForReady
             + " cpConfig: " + cpConfig;
             if (origAddress != null) {
                 output = output + " Orig Address: " + origAddress.toString();
             }
             if (origAddress != null) {
                 output = output + " Dest Address: " + destAddress.toString();
             }
             return output;
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

        public void setSpc(final long spcAsLong) {
            this.spc = convertToByte(spcAsLong);
        }
        
        private static byte[] convertToByte(final long spcAsLong) {
            return new byte[]{ Tools.getLoByteOf4(spcAsLong), Tools.getNextToLoByteOf4(spcAsLong), Tools.getNextToHiByteOf4(spcAsLong)};
        }
        
        @Override
        public String toString() {
            return "SSN: " + ssn + " SPC: " + EncodingHelper.bytesToHex(spc);
        }

    }
}
