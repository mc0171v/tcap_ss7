package com.vennetics.bell.sam.ss7.tcap.enabler.commands;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.BellSamTcapListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SendAtiCommandTest {

    @Mock
    private BellSamTcapListener mockListener;
    
    @Mock
    private IDialogue mockDialogue;

    @Test
    public void shouldSendAti() throws Exception {
        final Object oBAtiMessage = getRequestObject();
        when(mockListener.startDialogue(eq(oBAtiMessage), isA(CountDownLatch.class))).thenReturn(mockDialogue);
        when(mockDialogue.getResult()).thenReturn(null);
        final SendAtiCommand command = new SendAtiCommand(mockListener, (OutboundATIMessage) oBAtiMessage);
        assertThat(command.execute(), sameInstance(oBAtiMessage));
    }
    
    private Object getRequestObject() {
        final OutboundATIMessage oBAtiMessage = new OutboundATIMessage();
        oBAtiMessage.setImsi("12345678");
        oBAtiMessage.setRequestInfoLocation(true);
        return oBAtiMessage;
    }
}
