package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;

public class Ss7Address {

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
