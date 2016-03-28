
package com.vennetics.bell.sam.ss7.tcap.ati.simulator.response;

import com.vennetics.bell.sam.ss7.tcap.ati.simulator.map.SubscriberState;

public class ATIResponseMessage {
    
    private String imsi;
    private String msisdn;
    private Boolean requestInfoLocation;
    private Boolean requestInfoSubscriberState;
    
    //Result
    private SubscriberState status;
    private byte[] latitude;
    private byte[] longitude;
    private byte uncertainty;
    private Integer age;
    
    private String error;
    

    /**
     * Gets the value of the imsi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImsi() {
        return imsi;
    }

    /**
     * Sets the value of the imsi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImsi(final String value) {
        this.imsi = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(final String value) {
        this.error = value;
    }
    
    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsisdn(final String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the requestInfoLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Boolean getRequestInfoLocation() {
        return requestInfoLocation;
    }

    /**
     * Sets the value of the requestInfoLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequestInfoLocation(final Boolean value) {
        this.requestInfoLocation = value;
    }

    /**
     * Is the value of the requestInfoLocation property set.
     * 
     */
    public boolean isRequestInfoLocation() {
        if (this.requestInfoLocation != null) {
            return this.requestInfoLocation;
        }
        return false;
    }
    
    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriberState }
     *     
     */
    public SubscriberState getStatus() {
        return status;
    }
    
    /**
     * Gets the value of the age property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStatus(final SubscriberState value) {
        this.status = value;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAge(final Integer value) {
        this.age = value;
    }
    
    /**
     * Gets the value of the requestInfoSubscriberState property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getRequestInfoSubscriberState() {
        return requestInfoSubscriberState;
    }

    /**
     * Sets the value of the requestInfoSubscriberState property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequestInfoSubscriberState(final Boolean value) {
        this.requestInfoSubscriberState = value;
    }

    /**
     * Is the value of the requestInfoSubscriberState property set.
     * 
     */
    public boolean isRequestInfoSubscriberState() {
        if (this.requestInfoSubscriberState != null) {
            return this.requestInfoSubscriberState;
        }
        return false;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link byte[] }
     *     
     */
    public byte[] getLatitude() {
        return latitude;
    }
    
    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link byte[] }
     *     
     */
    public void setLatitude(final byte[] value) {
        this.latitude = value;
    }
    
    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link byte[] }
     *     
     */
    public byte[] getLongitude() {
        return longitude;
    }
    
    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link byte[] }
     *     
     */
    public void setLongitude(final byte[] value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the uncertainty property.
     * 
     * @return
     *     possible object is
     *     {@link byte }
     *     
     */
    public byte getUncertainty() {
        return uncertainty;
    }
    
    /**
     * Sets the value of the uncertainty property.
     * 
     * @param value
     *     allowed object is
     *     {@link byte }
     *     
     */
    public void setUncertainty(final byte value) {
        this.uncertainty = value;
    }
}
