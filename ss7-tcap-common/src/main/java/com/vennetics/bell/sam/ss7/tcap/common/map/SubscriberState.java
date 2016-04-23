package com.vennetics.bell.sam.ss7.tcap.common.map;

public enum SubscriberState {
    ASSUMED_IDLE,
    CAMEL_BUSY,
    NET_DET_NOT_REACHEABLE,
    NOT_PROVIDED_VLR,
    UNKOWN;
    
    public String value() {
        return name();
    }
}
