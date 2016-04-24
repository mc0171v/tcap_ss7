package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;

import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties.Ss7Address;

public interface ISs7ConfigurationProperties {

    /***
     * Get the configured GSM SCF Address
     * @return
     *     a string representing the GSM SCF Address
     */
    String getGsmScfAddress();
    
    /***
     * Set the configured GSM SCF Address
     * @param gsmScfAddress
     *     a string representing the GSM SCF Address
     */
    void setGsmScfAddress(String gsmScfAddress);
    
    /***
     * Get the configured stack standard
     * @return
     *     an integer representing the configured stack standard
     */
    int getStd();
    
    /***
     * Set the configured stack standard
     * @param sTd
     *     an integer representing the configured stack standard
     */
    void setStd(final int sTd);
    
    /***
     * Get the configured invoke timeout
     * @return
     *     the invoke timeout in milliseconds
     */
    long getInvokeTimeout();
    
    /***
     * Set the configured invoke timeout
     * @param invokeTimeout
     *     the invoke timeout in milliseconds
     */
    void setInvokeTimeout(long invokeTimeout);
    
    /***
     * Get the result latch timeout
     * @return
     *     the result latch timeout in milliseconds
     */
    long getLatchTimeout();
    
    /***
     * Set the result latch timeout
     * @param latchTimeout
     *     the result latch timeout in milliseconds
     */
    void setLatchTimeout(long latchTimeout);
    
    /***
     * Get the configured SS7 originating address for the TCAP listener
     * @return
     *     the configured SS7 originating address {@link Ss7Address}
     */
    Ss7Address getOrigAddress();
    
    /***
     * Set the configured SS7 originating address for the TCAP listener
     * @param origiAddress
     *     the configured SS7 originating address {@link Ss7Address}
     */
    void setOrigAddress(Ss7Address origiAddress);
    
    /***
     * Get the configured SS7 destination address for the TCAP listener
     * @return
     *     the configured SS7 destination address {@link Ss7Address}
     */
    Ss7Address getDestAddress();
    
    /***
     * Set the configured SS7 destination address for the TCAP listener
     * @param destAddress
     *     the configured SS7 destination address {@link Ss7Address}
     */
    void setDestAddress(Ss7Address destAddress);
    
    /***
     * Get the number of retry attempts in unbound state waiting for bind before failing
     * @return
     *    the number of retry attempts in unbound state
     */
    int getWaitForBindRetries();
    
    /***
     * Set the number of retry attempts in unbound state waiting for bind before failing
     * @param waitForBindRetries
     *     the number of retry attempts in unbound state
     */
    void setWaitForBindRetries(int waitForBindRetries);
    
    /***
     * Get the number of retry attempts in bound state waiting for ready before failing
     * @return
     *     the number of retry attempts in bound state
     */
    int getWaitForReadyRetries();
    
    /***
     * Set the number of retry attempts in bound state waiting for ready before failing
     * @param waitForReadyRetries
     *     the number of retry attempts in bound state
     */
    void setWaitForReadyRetries(int waitForReadyRetries);
    
    /***
     * Get the wait in milliseconds before a bind retry
     * @return
     *     the wait in milliseconds before a bind retry
     */
    long getWaitBeforeBindRetry();
    
    /***
     * Get the wait in milliseconds before a bind retry
     * @param waitBeforeBindRetry
     *     the wait in milliseconds before a bind retry
     */
    void setWaitBeforeBindRetry(long waitBeforeBindRetry);
    
    /***
     * Get the wait in milliseconds before a ready retry
     * @return
     *     the wait in milliseconds before a ready retry
     */
    long getWaitBeforeReadyRetry();
    
    /***
     * Set the wait in milliseconds before a ready retry
     * @param waitBeforeReadyRetry
     *     the wait in milliseconds before a ready retry
     */
    void setWaitBeforeReadyRetry(long waitBeforeReadyRetry);
    
    /***
     * Do we wait for listener ready state
     * @return
     *     true or false
     */
    boolean isWaitForReady();
    
    /***
     * Set whether to wait for listener ready state
     * @param waitForReady
     *     true or false
     */
    void setWaitForReady(boolean waitForReady);
    
    /***
     * Get the String that represents the common parts configuration
     * @return
     *     the common parts configuration
     */
    String getCpConfig();
    
    /***
     * Set the String that represents the common parts configuration
     * @param cpConfig
     *     the common parts configuration
     */
    void setCpConfig(String cpConfig);
    
    /***
     * Get the simulator operation type
     * @return
     *     the simulator operation type
     */
    public String getSimulatorOperation();
    
    /***
     * Set the simulator operation type
     * @param simulatorOperation
     *     the simulator operation type
     */
    void setSimulatorOperation(final String simulatorOperation);
}
