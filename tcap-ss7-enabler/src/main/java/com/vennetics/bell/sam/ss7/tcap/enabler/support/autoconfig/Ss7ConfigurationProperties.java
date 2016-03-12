package com.vennetics.bell.sam.ss7.tcap.enabler.support.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;

@ConfigurationProperties(prefix = "ss7")
public class Ss7ConfigurationProperties {

    private Ss7Address origAddress;
    private Ss7Address destAddress;
    private int sTD;

    Ss7ConfigurationProperties() {
        final byte[] spcA = { // signaling point 2143
                Tools.getLoByteOf2(231), // zone
                3, 0 };
        this.origAddress = new Ss7Address(spcA, (short) 99);
        this.destAddress = new Ss7Address(spcA, (short) 98);
        this.sTD = DialogueConstants.PROTOCOL_VERSION_ITU_97;
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
        return sTD;
    }

    public void setsTD(final int sTD) {
        this.sTD = sTD;
    }


    public static class Ss7Address {
        private short sSN;
        private byte[] sPC;
        
        Ss7Address(final byte[] sPc, final short sSn) {
            this.sPC = sPc;
            this.sSN = sSn;
        }
        
        public short getsSn() {
            return sSN;
        }
        
        public void setsSn(final short sSn) {
            this.sSN = sSn;
        }
        
        public byte[] getsPC() {
            return sPC;
        }
        
        public void setsPC(final byte[] sPC) {
            this.sPC = sPC;
        }
    }
}