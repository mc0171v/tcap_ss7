package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import jain.protocol.ss7.tcap.JainTcapProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.DialogueManager;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.DialogueExistsException;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.NoDialogueExistsException;
import com.vennetics.bell.sam.ss7.tcap.enabler.listener.SamTcapEventListener;

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
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.activate(mockDialogue);
        when(mockDialogue2.getDialogueId()).thenReturn(DIALOGUE_ID2);
        objectToTest.activate(mockDialogue2);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(mockDialogue));
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID2), sameInstance(mockDialogue2));
        assertTrue(objectToTest.isDialogueLeft());
        objectToTest.clearAllDialogs();
        assertTrue(!objectToTest.isDialogueLeft());
    }

    @Test(expected = DialogueExistsException.class)
    public void shouldThrowExceptionIfDialogueExists() throws Exception {
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        when(mockDialogue.getDialogueId()).thenReturn(DIALOGUE_ID);
        objectToTest.activate(mockDialogue);
        objectToTest.activate(mockDialogue);

    }

    @Test(expected = NoDialogueExistsException.class)
    public void shouldThrowExceptionIfNoDialogueExists() throws Exception {
        objectToTest.deactivate(mockDialogue);
    }

}
