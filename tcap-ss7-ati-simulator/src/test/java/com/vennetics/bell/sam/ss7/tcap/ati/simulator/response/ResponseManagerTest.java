package com.vennetics.bell.sam.ss7.tcap.ati.simulator.response;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class ResponseManagerTest {

    private IResponseMgr objectToTest = new ResponseMgr();

    private static final String IMSI = "12345";
    private static final String MSISDN = "98765";
    @Before
    public void setup() {
    }

    @Test
    public void shouldCreateAndDeleteResponseImsi() throws Exception {
        final ATIResponseMessage response = getResponseImsi();
        objectToTest.create(response);
        assertThat(objectToTest.lookUp(IMSI), sameInstance(response));
        objectToTest.delete(IMSI);
    }
    
    @Test
    public void shouldCreateAndDeleteResponseMsisdn() throws Exception {
        final ATIResponseMessage response = getResponseMsisdn();
        objectToTest.create(response);
        assertThat(objectToTest.lookUp(MSISDN), sameInstance(response));
        objectToTest.delete(MSISDN);
    }
    
    @Test
    public void shouldCreateAndDeleteResponseBoth() throws Exception {
        final ATIResponseMessage response = getResponseBoth();
        objectToTest.create(response);
        assertNull(objectToTest.lookUp(MSISDN));
        assertThat(objectToTest.lookUp(IMSI), sameInstance(response));
        objectToTest.delete(IMSI);
    }
    
    private ATIResponseMessage getResponseImsi() {
        final ATIResponseMessage response = new ATIResponseMessage();
        response.setImsi(IMSI);
        return response;
    }
    
    private ATIResponseMessage getResponseMsisdn() {
        final ATIResponseMessage response = new ATIResponseMessage();
        response.setMsisdn(MSISDN);
        return response;
    }
    
    private ATIResponseMessage getResponseBoth() {
        final ATIResponseMessage response = new ATIResponseMessage();
        response.setMsisdn(MSISDN);
        response.setImsi(IMSI);
        return response;
    }

}
