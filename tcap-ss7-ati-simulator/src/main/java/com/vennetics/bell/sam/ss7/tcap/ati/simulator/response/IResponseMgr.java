package com.vennetics.bell.sam.ss7.tcap.ati.simulator.response;

public interface IResponseMgr {

    ATIResponseMessage lookUp(String address);
    
    boolean create(ATIResponseMessage response);
}
