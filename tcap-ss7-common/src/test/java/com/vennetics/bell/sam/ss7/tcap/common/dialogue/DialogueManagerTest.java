package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import jain.protocol.ss7.tcap.JainTcapProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.einss7.jtcap.TcDialoguesLostIndEvent;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.SamTcapEventListener;

@RunWith(MockitoJUnitRunner.class)
public class DialogueManagerTest {

    private IDialogueManager objectToTest = new DialogueManager();

    private static final int DIALOGUE_ID = 1111;
    private static final int DIALOGUE_ID2 = 1112;
    // private static final int SSN = 8;

    @Mock
    private SamTcapEventListener mockListener;
    @Mock
    private JainTcapProvider mockProvider;
    @Mock
    private IDialogue mockDialogue;
    @Mock
    private IDialogue mockDialogue2;
    @Mock
    private TcDialoguesLostIndEvent mockEvent;

    @Before
    public void setup() {
        objectToTest.clearAllDialogs();
    }

    @Test
    public void shouldActivateDialogue() throws Exception {
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.activate(mockDialogue);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(mockDialogue));
    }

    @Test
    public void shouldDeActivateDialogue() throws Exception {
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.activate(mockDialogue);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(mockDialogue));
        objectToTest.deactivate(mockDialogue);
        assertTrue(!objectToTest.isDialogueLeft());
    }

    @Test
    public void shouldClearAllDialogue() throws Exception {
        addTwoDialogues();
        objectToTest.clearAllDialogs();
        assertTrue(!objectToTest.isDialogueLeft());
    }
    
    @Test
    public void shouldCleanupLostDialogue() throws Exception {
        addTwoDialogues();
        when(mockEvent.isDialogueIdLost(DIALOGUE_ID)).thenReturn(true);
        when(mockEvent.isDialogueIdLost(DIALOGUE_ID2)).thenReturn(false);
        objectToTest.cleanUpLostDialogues(mockEvent);
        assertNull(objectToTest.lookUpDialogue(DIALOGUE_ID));
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID2), sameInstance(mockDialogue2));
    }

    @Test(expected = Ss7ServiceException.class)
    public void shouldThrowExceptionIfDialogueExists() throws Exception {
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.activate(mockDialogue);
        objectToTest.activate(mockDialogue);

    }

    @Test(expected = Ss7ServiceException.class)
    public void shouldThrowExceptionIfNoDialogueExists() throws Exception {
        objectToTest.deactivate(mockDialogue);
    }
    
    private void addTwoDialogues() {
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.activate(mockDialogue);
        when(mockDialogue2.getDialogueId()).thenReturn(DIALOGUE_ID2);
        objectToTest.activate(mockDialogue2);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(mockDialogue));
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID2), sameInstance(mockDialogue2));
        assertTrue(objectToTest.isDialogueLeft());
    }

}
