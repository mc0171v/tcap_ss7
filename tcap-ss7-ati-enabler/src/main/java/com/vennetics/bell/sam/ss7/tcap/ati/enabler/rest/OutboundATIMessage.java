
package com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jvnet.jaxb2_commons.lang.CopyStrategy2;
import org.jvnet.jaxb2_commons.lang.CopyTo2;
import org.jvnet.jaxb2_commons.lang.Equals2;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy2;
import org.jvnet.jaxb2_commons.lang.HashCode2;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy2;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

import com.vennetics.bell.sam.ss7.tcap.common.error.IError;


/**
 * <p>Java class for InboundATIMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutboundATIMessage"&gt;
 *   &lt;complexContent&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="imsi" type="String"/&gt;
 *           &lt;element name="msisdn" type="String"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="requestInfoLocation" type="boolean"/&gt;
 *         &lt;element name="requestInfoSubscriberState" type="boolean"/&gt;
 *         &lt;element name="correlator" type="String"/&gt;
 *         &lt;element name="status" type="String"/&gt;
 *         &lt;element name="latitude" type="byte[]"/&gt;
 *         &lt;element name="longitude" type="byte[]"/&gt;
 *         &lt;element name="uncertainty" type="byte"/&gt;
 *         &lt;element name="age" type="Integer"/&gt;
 *         &lt;element name="error" type="String"/&gt;
 *       &lt;/sequence&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutboundATIMessage", propOrder = {
    "imsi",
    "msisdn",
    "requestInfoLocation",
    "requestInfoSubscriberState",
    "correlator",
    "status",
    "latitude",
    "longitude",
    "uncertainty",
    "age",
    "error"
})
@XmlRootElement(name = "OutboundATIMessage")
public class OutboundATIMessage implements Cloneable, CopyTo2, Equals2, HashCode2, ToString2, IError {

    private String imsi;
    private String msisdn;
    private Boolean requestInfoLocation;
    private Boolean requestInfoSubscriberState;
    private String correlator;
    
    //Result
    private Integer status;
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
     * Gets the value of the correlator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelator() {
        return correlator;
    }

    /**
     * Sets the value of the correlator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelator(final String value) {
        this.correlator = value;
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
     *     {@link Integer }
     *     
     */
    public Integer getStatus() {
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
    public void setStatus(final Integer value) {
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
    
    public OutboundATIMessage withImsi(final String value) {
        setImsi(value);
        return this;
    }

    public OutboundATIMessage withMsisdn(final String value) {
        setMsisdn(value);
        return this;
    }

    public OutboundATIMessage withRequestInfoLocation(final Boolean value) {
        setRequestInfoLocation(value);
        return this;
    }

    public OutboundATIMessage withStatus(final Integer value) {
        setStatus(value);
        return this;
    }

    public OutboundATIMessage withAge(final Integer value) {
        setAge(value);
        return this;
    }
    public OutboundATIMessage withRequestInfoSubscriberState(final Boolean value) {
        setRequestInfoSubscriberState(value);
        return this;
    }
    
    public OutboundATIMessage withCorrelator(final String value) {
        setCorrelator(value);
        return this;
    }

    public OutboundATIMessage withError(final String value) {
        setError(value);
        return this;
    }

    public OutboundATIMessage withLongitude(final byte[] value) {
        setLongitude(value);
        return this;
    }
    
    public OutboundATIMessage withLatitude(final byte[] value) {
        setLatitude(value);
        return this;
    }
    
    public OutboundATIMessage withUncertainty(final byte value) {
        setUncertainty(value);
        return this;
    }
    
    public String toString() {
        final ToStringStrategy2 strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(final ObjectLocator locator, final StringBuilder buffer, final ToStringStrategy2 strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(final ObjectLocator locator,
                                      final StringBuilder buffer,
                                      final ToStringStrategy2 strategy) {
        String theImsi;
        theImsi = this.getImsi();
        strategy.appendField(locator, this, "imsi", buffer, theImsi, this.imsi != null);
        
        String theMsisdn;
        theMsisdn = this.getMsisdn();
        strategy.appendField(locator, this, "msisdn", buffer, theMsisdn, this.msisdn != null);
        
        Boolean theRequestInfoLocation;
        theRequestInfoLocation = this.getRequestInfoLocation();
        strategy.appendField(locator,
                             this,
                             "requestInfoLocation",
                             buffer,
                             theRequestInfoLocation,
                             this.requestInfoLocation != null);
        
        Integer theStatus;
        theStatus = this.getStatus();
        strategy.appendField(locator, this, "status", buffer, theStatus, this.status != null);

        Integer theAge;
        theAge = this.getAge();
        strategy.appendField(locator, this, "age", buffer, theAge, this.age != null);
        
        Boolean theRequestInfoSubscriberState;
        theRequestInfoSubscriberState = this.getRequestInfoSubscriberState();
        strategy.appendField(locator,
                             this,
                             "requestInfoSubscriberState",
                             buffer,
                             theRequestInfoSubscriberState,
                             this.requestInfoSubscriberState != null);
        
        String theCorrelator;
        theCorrelator = this.getCorrelator();
        strategy.appendField(locator,
                             this,
                             "correlator",
                             buffer,
                             theCorrelator,
                             this.correlator != null);

        String theError;
        theError = this.getError();
        strategy.appendField(locator,
                             this,
                             "error",
                             buffer,
                             theError,
                             this.error != null);
        
        byte[] theLatitude;
        theLatitude = this.getLatitude();
        strategy.appendField(locator,
                             this,
                             "latitude",
                             buffer,
                             theLatitude,
                             this.latitude != null);
       
        byte[] theLongitude;
        theLongitude = this.getLongitude();
        strategy.appendField(locator,
                             this,
                             "longitude",
                             buffer,
                             theLongitude,
                             this.longitude != null);
       
        byte theUncertainty;
        theUncertainty = this.getUncertainty();
        strategy.appendField(locator,
                             this,
                             "uncertainty",
                             buffer,
                             theUncertainty,
                             true);
        return buffer;
    }

    public int hashCode(final ObjectLocator locator, final HashCodeStrategy2 strategy) {
        int currentHashCode = 1;

            String theImsi;
            theImsi = this.getImsi();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "imsi", theImsi), currentHashCode, theImsi, this.imsi != null);

            String theMsisdn;
            theMsisdn = this.getMsisdn();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "msisdn", theMsisdn), currentHashCode, theMsisdn, this.msisdn != null);

            Boolean theRequestInfoLocation;
            theRequestInfoLocation = this.getRequestInfoLocation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
                                                                      "requestInfoLocation",
                                                                      theRequestInfoLocation),
                                                currentHashCode,
                                                theRequestInfoLocation,
                                                this.requestInfoLocation != null);

            Integer theStatus;
            theStatus = this.getStatus();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
                                                                      "status",
                                                                      theStatus),
                                                currentHashCode,
                                                theStatus,
                                                this.status != null);

            Integer theAge;
            theAge = this.getAge();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
                                                                      "age",
                                                                      theAge),
                                                currentHashCode,
                                                theAge,
                                                this.age != null);
            
            Boolean theRequestInfoSubscriberState;
            theRequestInfoSubscriberState = this.getRequestInfoSubscriberState();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
                                                                      "requestInfoSubscriberState",
                                                                      theRequestInfoSubscriberState),
                                                currentHashCode, theRequestInfoSubscriberState,
                                                this.requestInfoSubscriberState != null);

            String theCorrelator;
            theCorrelator = this.getCorrelator();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "correlator", theCorrelator), currentHashCode, theCorrelator, this.correlator != null);

            String theError;
            theError = this.getError();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "error", theError), currentHashCode, theError, this.error != null);
            
            byte[] theLongitude;
            theLongitude = this.getLongitude();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "longitude", theLongitude), currentHashCode, theLongitude, this.longitude != null);
            
            byte[] theLatitude;
            theLatitude = this.getLatitude();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "latitude", theLatitude), currentHashCode, theLatitude, this.latitude != null);
            
            byte theUncertainty;
            theUncertainty = this.getUncertainty();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "uncertainty", theUncertainty), currentHashCode, theUncertainty, true);

            return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy2 strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public boolean equals(final ObjectLocator thisLocator,
                          final ObjectLocator thatLocator,
                          final Object object,
                          final EqualsStrategy2 strategy) {
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final OutboundATIMessage that = ((OutboundATIMessage) object);
            String lhsImsi;
            lhsImsi = this.getImsi();
            String rhsImsi;
            rhsImsi = that.getImsi();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "imsi", lhsImsi),
                                 LocatorUtils.property(thatLocator, "imsi", rhsImsi),
                                 lhsImsi,
                                 rhsImsi,
                                 this.imsi != null,
                                 that.imsi != null)) {
                return false;
            }
            
            String lhsMsisdn;
            lhsMsisdn = this.getMsisdn();
            String rhsMsisdn;
            rhsMsisdn = that.getMsisdn();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "msisdn", lhsMsisdn),
                                 LocatorUtils.property(thatLocator, "msisdn", rhsMsisdn),
                                 lhsMsisdn,
                                 rhsMsisdn,
                                 this.msisdn != null,
                                 that.msisdn != null)) {
                return false;
            }
            
            Boolean lhsRequestInfoLocation;
            lhsRequestInfoLocation = this.getRequestInfoLocation();
            Boolean rhsRequestInfoLocation;
            rhsRequestInfoLocation = that.getRequestInfoLocation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestInfoLocation", lhsRequestInfoLocation),
                                 LocatorUtils.property(thatLocator, "requestInfoLocation",  rhsRequestInfoLocation),
                                 lhsRequestInfoLocation, rhsRequestInfoLocation,
                                 this.requestInfoLocation != null,
                                 that.requestInfoLocation != null)) {
                return false;
            }
            
            Integer lhsStatus;
            lhsStatus = this.getStatus();
            Integer rhsStatus;
            rhsStatus = that.getStatus();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "state", lhsStatus),
                                 LocatorUtils.property(thatLocator, "state", rhsStatus),
                                 lhsStatus, rhsStatus,
                                 this.status != null,
                                 that.status != null)) {
                return false;
            }

            Integer lhsAge;
            lhsAge = this.getAge();
            Integer rhsAge;
            rhsAge = that.getAge();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "status", lhsAge),
                                 LocatorUtils.property(thatLocator, "status", rhsAge),
                                 lhsAge, rhsAge,
                                 this.age != null,
                                 that.age != null)) {
                return false;
            }
            
            Boolean lhsRequestInfoSubscriberState;
            lhsRequestInfoSubscriberState = this.getRequestInfoSubscriberState();
            Boolean rhsRequestInfoSubscriberState;
            rhsRequestInfoSubscriberState = that.getRequestInfoSubscriberState();
            if (!strategy.equals(LocatorUtils.property(thisLocator,
                                                       "requestInfoSubscriberState",
                                                       lhsRequestInfoSubscriberState),
                                 LocatorUtils.property(thatLocator, "requestInfoSubscriberState",
                                                       rhsRequestInfoSubscriberState),
                                 lhsRequestInfoSubscriberState,
                                 rhsRequestInfoSubscriberState,
                                 this.requestInfoSubscriberState != null,
                                 that.requestInfoSubscriberState != null)) {
                return false;
            }
            
            String lhsCorrelator;
            lhsCorrelator = this.getCorrelator();
            String rhsCorrelator;
            rhsCorrelator = that.getCorrelator();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "correlator", lhsCorrelator),
                                 LocatorUtils.property(thatLocator, "correlator", rhsCorrelator),
                                 lhsCorrelator,
                                 rhsCorrelator,
                                 this.correlator != null,
                                 that.correlator != null)) {
                return false;
            }
            
            String lhsError;
            lhsError = this.getError();
            String rhsError;
            rhsError = that.getError();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "error", lhsError),
                                 LocatorUtils.property(thatLocator, "error", rhsError),
                                 lhsError,
                                 rhsError,
                                 this.error != null,
                                 that.error != null)) {
                return false;
            }
            
            byte[] lhsLongitude;
            lhsLongitude = this.getLongitude();
            byte[] rhsLongitude;
            rhsLongitude = that.getLongitude();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "longitude", lhsLongitude),
                                 LocatorUtils.property(thatLocator, "longitude", rhsLongitude),
                                 lhsLongitude,
                                 rhsLongitude,
                                 this.longitude != null,
                                 that.longitude != null)) {
                return false;
            }
            
            byte[] lhsLatitude = this.getLatitude();
            byte[] rhsLatitude = that.getLatitude();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "latitude", lhsLatitude),
                                 LocatorUtils.property(thatLocator, "latitude", rhsLatitude),
                                 lhsLatitude,
                                 rhsLatitude,
                                 this.latitude != null,
                                 that.latitude != null)) {
                return false;
            }
            
            byte lhsUncertainty;
            lhsUncertainty = this.getUncertainty();
            byte rhsUncertainty;
            rhsUncertainty = that.getUncertainty();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "uncertainty", lhsUncertainty),
                                 LocatorUtils.property(thatLocator, "uncertainty", rhsUncertainty),
                                 lhsUncertainty,
                                 rhsUncertainty,
                                 true,
                                 true)) {
                return false;
            }
        return true;
    }

    public boolean equals(final Object object) {
        final EqualsStrategy2 strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(final Object target) {
        final CopyStrategy2 strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(final ObjectLocator locator, final Object target, final CopyStrategy2 strategy) {
        final Object draftCopy = ((target == null) ? createNewInstance() : target);
        if (draftCopy instanceof OutboundATIMessage) {
            final OutboundATIMessage copy = ((OutboundATIMessage) draftCopy);
                Boolean imsiShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.imsi != null);
                if (imsiShouldBeCopiedAndSet == Boolean.TRUE) {
                    String sourceImsi;
                    sourceImsi = this.getImsi();
                    String copyImsi = ((String) strategy.copy(LocatorUtils.property(locator, "imsi", sourceImsi), sourceImsi, this.imsi != null));
                    copy.setImsi(copyImsi);
                } else {
                    if (imsiShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.imsi = null;
                    }
                }
                
                Boolean msisdnShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.msisdn != null);
                if (msisdnShouldBeCopiedAndSet == Boolean.TRUE) {
                    String sourceMsisdn;
                    sourceMsisdn = this.getMsisdn();
                    String copyMsisdn = ((String) strategy.copy(LocatorUtils.property(locator, "msisdn", sourceMsisdn), sourceMsisdn, this.msisdn != null));
                    copy.setMsisdn(copyMsisdn);
                } else {
                    if (msisdnShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.msisdn = null;
                    }
                }

                Boolean requestInfoLocatioShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.requestInfoLocation != null);
                if (requestInfoLocatioShouldBeCopiedAndSet == Boolean.TRUE) {
                    Boolean sourceRequestInfoLocation;
                    sourceRequestInfoLocation = this.getRequestInfoLocation();
                    Boolean copyRequestInfoLocation = ((Boolean) strategy.copy(LocatorUtils.property(locator, "requestInfoLocation", sourceRequestInfoLocation),
                                                                               sourceRequestInfoLocation,
                                                                               this.requestInfoLocation != null));
                    copy.setRequestInfoLocation(copyRequestInfoLocation);
                } else {
                    if (requestInfoLocatioShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.requestInfoLocation = null;
                    }
                }
                
                Boolean statusShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.status != null);
                if (statusShouldBeCopiedAndSet == Boolean.TRUE) {
                    Integer sourceStatus;
                    sourceStatus = this.getStatus();
                    Integer copyStatus = ((Integer) strategy.copy(LocatorUtils.property(locator, "status", sourceStatus),
                                                                               sourceStatus,
                                                                               this.status != null));
                    copy.setStatus(copyStatus);
                } else {
                    if (statusShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.status = null;
                    }
                }

                Boolean ageShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.age != null);
                if (ageShouldBeCopiedAndSet == Boolean.TRUE) {
                    Integer sourceAge;
                    sourceAge = this.getAge();
                    Integer copyAge = ((Integer) strategy.copy(LocatorUtils.property(locator, "age", sourceAge),
                                                                               sourceAge,
                                                                               this.age != null));
                    copy.setAge(copyAge);
                } else {
                    if (ageShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.age = null;
                    }
                }
                
                Boolean requestInfoSubscriberStateShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.requestInfoSubscriberState != null);
                if (requestInfoSubscriberStateShouldBeCopiedAndSet == Boolean.TRUE) {
                    Boolean sourceRequestInfoSubscriberState;
                    sourceRequestInfoSubscriberState = this.getRequestInfoSubscriberState();
                    Boolean copyRequestInfoSubscriberState = ((Boolean) strategy.copy(LocatorUtils.property(locator,
                                                                                                            "resourceURL",
                                                                                                            sourceRequestInfoSubscriberState),
                                                                                      sourceRequestInfoSubscriberState,
                                                                                      this.requestInfoSubscriberState != null));
                    copy.setRequestInfoSubscriberState(copyRequestInfoSubscriberState);
                } else {
                    if (requestInfoSubscriberStateShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.requestInfoSubscriberState = null;
                    }
                }
                
                Boolean correlatorShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.correlator != null);
                if (correlatorShouldBeCopiedAndSet == Boolean.TRUE) {
                    String sourceCorrelator;
                    sourceCorrelator = this.getCorrelator();
                    String copyCorrelator = ((String) strategy.copy(LocatorUtils.property(locator, "correlator", sourceCorrelator), sourceCorrelator, this.correlator != null));
                    copy.setCorrelator(copyCorrelator);
                } else {
                    if (correlatorShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.correlator = null;
                    }
                }
                 
                Boolean errorShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.error != null);
                if (errorShouldBeCopiedAndSet == Boolean.TRUE) {
                    String sourceError;
                    sourceError = this.getError();
                    String copyError = ((String) strategy.copy(LocatorUtils.property(locator, "error", sourceError), sourceError, this.error != null));
                    copy.setError(copyError);
                } else {
                    if (errorShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.error = null;
                    }
                }
                
                Boolean longitudeShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.longitude != null);
                if (longitudeShouldBeCopiedAndSet == Boolean.TRUE) {
                    byte[] sourceLongitude;
                    sourceLongitude = this.getLongitude();
                    byte[] copyLongitude = ((byte[]) strategy.copy(LocatorUtils.property(locator, "longitude", sourceLongitude), sourceLongitude, this.longitude != null));
                    copy.setLongitude(copyLongitude);
                } else {
                    if (longitudeShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.longitude = null;
                    }
                }
                
                Boolean latitudeShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, this.latitude != null);
                if (longitudeShouldBeCopiedAndSet == Boolean.TRUE) {
                    byte[] sourceLatitude;
                    sourceLatitude = this.getLatitude();
                    byte[] copyLatitude = ((byte[]) strategy.copy(LocatorUtils.property(locator, "latitude", sourceLatitude), sourceLatitude, this.latitude != null));
                    copy.setLatitude(copyLatitude);
                } else {
                    if (latitudeShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.latitude = null;
                    }
                }
                
                Boolean uncertaintyShouldBeCopiedAndSet = strategy.shouldBeCopiedAndSet(locator, true);
                if (uncertaintyShouldBeCopiedAndSet == Boolean.TRUE) {
                    byte sourceUncertainty;
                    sourceUncertainty = this.getUncertainty();
                    byte copyUncertainty = ((byte) strategy.copy(LocatorUtils.property(locator, "uncertainty", sourceUncertainty), sourceUncertainty, true));
                    copy.setUncertainty(copyUncertainty);
                } else {
                    if (uncertaintyShouldBeCopiedAndSet == Boolean.FALSE) {
                        copy.uncertainty = 0x0;
                    }
                }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new OutboundATIMessage();
    }
    
    public String getDestination() {
        if (this.msisdn != null) {
            return this.msisdn;
        }
        return this.imsi;
    }

}
