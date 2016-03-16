package com.vennetics.bell.sam.ss7.tcap.enabler.commands;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.listener.states.ListenerBound;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.SamTcapEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SendAtiCommandTest {

    @Mock
    private SamTcapEventListener mockListener;
    
    @Mock
    private IDialogue mockDialogue;
    
    private CountDownLatch latch;
    
    @Before
    public void setup() throws Exception {
    }
    

    @Test
    public void shouldSendAti() throws Exception {
        final Object oBAtiMessage = getRequestObject();
        latch = new CountDownLatch(1);
        when(mockListener.startDialogue(eq(oBAtiMessage), eq(latch))).thenReturn(mockDialogue);
        latch.countDown();
        when(mockDialogue.getResult()).thenReturn(null);
        final SendAtiCommand command = new SendAtiCommand(mockListener,
                                                          (OutboundATIMessage) oBAtiMessage,
                                                          latch);
        assertThat(command.execute(), sameInstance(oBAtiMessage));
    }
    
    private Object getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }
}
