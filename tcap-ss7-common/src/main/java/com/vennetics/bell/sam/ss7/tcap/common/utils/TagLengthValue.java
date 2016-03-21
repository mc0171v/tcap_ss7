package com.vennetics.bell.sam.ss7.tcap.common.utils;

public class TagLengthValue {

    private byte tag;
    private byte[] length;
    private byte[] value;
    
    public void setTag(final byte tag) {
        this.tag = tag;
    }
    
    public void setLength(final byte[] length) {
        this.length = length;
    }
    
    public void setValue(final byte[] value) {
        this.value = value;
    }
    
    public byte getTag() {
        return tag;
    }
    
    public byte[] getLength() {
        return length;
    }
    
    public byte[] getValue() {
        return value;
    }
}
