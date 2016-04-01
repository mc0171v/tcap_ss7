package com.vennetics.bell.sam.ss7.tcap.ati.simulator.response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseMgr implements IResponseMgr {

    private final Map<String, ATIResponseMessage> responseMap = new ConcurrentHashMap<String, ATIResponseMessage>();
    
    
    @Override
    public ATIResponseMessage lookUp(final String address) {
        return responseMap.get(address);
    }


    @Override
    public boolean create(final ATIResponseMessage response) {
        final String address = getAddress(response);
        if (address != null) {
            responseMap.put(address, response);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(final String address) {
        if (lookUp(address) != null) {
            responseMap.remove(address);
            return true;
        }
        return false;
    }

    private static String getAddress(final ATIResponseMessage response) {
        if (response.getImsi() != null) {
            return response.getImsi();
        } else if (response.getMsisdn() != null) {
            return response.getMsisdn();
        }
        return null;
    }
}
