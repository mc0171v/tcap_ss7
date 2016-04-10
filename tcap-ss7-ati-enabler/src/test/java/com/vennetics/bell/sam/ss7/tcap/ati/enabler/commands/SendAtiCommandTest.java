package com.vennetics.bell.sam.ss7.tcap.ati.enabler.commands;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.when;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.ati.enabler.service.AtiService;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.SamTcapEventListener;
import com.vennetics.bell.sam.ss7.tcap.common.map.SubscriberState;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class SendAtiCommandTest {
	
    private static final Logger logger = LoggerFactory.getLogger(AtiService.class);

    @Mock
    private SamTcapEventListener mockListener;
    
    @Mock
    private IDialogue mockDialogue;
    
    private CountDownLatch latch;
    
    @Before
    public void setup() throws Exception {
    }
    

    @Test(expected = Ss7ServiceException.class)
    public void shouldSendAtiAndThrowExceptionOnNoResult() throws Throwable {
        final Object oBAtiMessage = getRequestObject();
        commonWhen(oBAtiMessage);
        when(mockDialogue.getResult()).thenReturn(null);
        final SendAtiCommand command = new SendAtiCommand(mockListener,
                                                          (OutboundATIMessage) oBAtiMessage,
                                                          latch);
        try {
        command.execute();
        } catch (HystrixRuntimeException ex) {
        	throw ex.getCause();
        }
    }
    
    @Test
    public void shouldSendAtiAndReturnResult() throws Exception {
        final Object oBAtiMessage = getRequestObject();
        final Object resOBAtiMessage = getResultObject();
        commonWhen(oBAtiMessage);
        when(mockDialogue.getResult()).thenReturn(resOBAtiMessage);
        final SendAtiCommand command = new SendAtiCommand(mockListener,
                                                          (OutboundATIMessage) oBAtiMessage,
                                                          latch);
        assertThat(command.execute(), sameInstance(resOBAtiMessage));
    }
    
    @Test(expected = Ss7ServiceException.class)
    public void shouldSendAtiAndThrowExceptionOnResultWithError() throws Throwable {
        final Object oBAtiMessage = getRequestObject();
        final Object resOBAtiMessage = getResultObject();
        ((OutboundATIMessage)resOBAtiMessage).setError(new Ss7ServiceException("ss7 error"));
        commonWhen(oBAtiMessage);
        latch.countDown();
        when(mockDialogue.getResult()).thenReturn(resOBAtiMessage);
        final SendAtiCommand command = new SendAtiCommand(mockListener,
                                                          (OutboundATIMessage) oBAtiMessage,
                                                          latch);
        try {
        command.execute();
        } catch (HystrixRuntimeException ex) {
        	throw ex.getCause();
        }
    }
    
    private void commonWhen(final Object oBAtiMessage) {
        final ISs7ConfigurationProperties props = new Ss7ConfigurationProperties();
        props.setLatchTimeout(1000);
        latch = new CountDownLatch(1);
        when(mockListener.startDialogue(eq(oBAtiMessage), eq(latch))).thenReturn(mockDialogue);
        when(mockListener.getConfigProperties()).thenReturn(props);
        latch.countDown();   	
    }
    
    private Object getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }
    
    private Object getResultObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        oBAtiMessage.setStatus(SubscriberState.CAMEL_BUSY);
        return oBAtiMessage;
    }
}
