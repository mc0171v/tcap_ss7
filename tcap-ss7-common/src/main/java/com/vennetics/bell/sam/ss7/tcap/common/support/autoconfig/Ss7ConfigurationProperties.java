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
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getOrigAddress()
     */
    @Override
    public Ss7Address getOrigAddress() {
        return origAddress;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#
     * setOrigAddress(com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties.Ss7Address)
     */
    @Override
    public void setOrigAddress(final Ss7Address origiAddress) {
        this.origAddress = origiAddress;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getDestAddress()
     */
    @Override
    public Ss7Address getDestAddress() {
        return destAddress;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#
     * setDestAddress(com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties.Ss7Address)
     */
    @Override
    public void setDestAddress(final Ss7Address destAddress) {
        this.destAddress = destAddress;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getStd()
     */
    @Override
    public int getStd() {
        return std;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setStd(int)
     */
    @Override
    public void setStd(final int sTD) {
        this.std = sTD;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getGsmScfAddress()
     */
    @Override
    public String getGsmScfAddress() {
        return gsmScfAddress;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setGsmScfAddress(java.lang.String)
     */
    @Override
    public void setGsmScfAddress(final String gsmScfAddress) {
        this.gsmScfAddress = gsmScfAddress;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getInvokeTimeout()
     */
    @Override
    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setInvokeTimeout(long)
     */
    @Override
    public void setInvokeTimeout(final long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getLatchTimeout()
     */
    @Override
    public long getLatchTimeout() {
        return latchTimeout;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setLatchTimeout(long)
     */
    @Override
    public void setLatchTimeout(final long latchTimeout) {
        this.latchTimeout = latchTimeout;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getWaitForBindRetries()
     */
    @Override
    public int getWaitForBindRetries() {
        return waitForBindRetries;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setWaitForBindRetries(int)
     */
    @Override
    public void setWaitForBindRetries(final int waitForBindRetries) {
        this.waitForBindRetries = waitForBindRetries;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getWaitForReadyRetries()
     */
    @Override
    public int getWaitForReadyRetries() {
        return waitForReadyRetries;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setWaitForReadyRetries(int)
     */
    @Override
    public void setWaitForReadyRetries(final int waitForReadyRetries) {
        this.waitForReadyRetries = waitForReadyRetries;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getWaitBeforeBindRetry()
     */
    @Override
    public long getWaitBeforeBindRetry() {
        return waitBeforeBindRetry;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setWaitBeforeBindRetry(long)
     */
    @Override
    public void setWaitBeforeBindRetry(final long waitBeforeBindRetry) {
        this.waitBeforeBindRetry = waitBeforeBindRetry;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getWaitBeforeReadyRetry()
     */
    @Override
    public long getWaitBeforeReadyRetry() {
        return waitBeforeReadyRetry;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setWaitBeforeReadyRetry(long)
     */
    @Override
    public void setWaitBeforeReadyRetry(final long waitBeforeReadyRetry) {
        this.waitBeforeReadyRetry = waitBeforeReadyRetry;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#isWaitForReady()
     */
    @Override
    public boolean isWaitForReady() {
        return waitForReady;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setWaitForReady(boolean)
     */
    @Override
    public void setWaitForReady(final boolean waitForReady) {
        this.waitForReady = waitForReady;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#getCpConfig()
     */
    @Override
    public String getCpConfig() {
        return cpConfig;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties#setCpConfig(java.lang.String)
     */
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
