package com.vennetics.bell.sam.ss7.tcap.enabler.service;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import jain.protocol.ss7.tcap.JainTcapProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vennetics.bell.sam.ss7.tcap.enabler.exception.DialogueExistsException;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.NoDialogueExistsException;

@RunWith(MockitoJUnitRunner.class)
public class DialogueManagerTest {

    private IDialogueManager objectToTest = new DialogueManager();

    private static final int DIALOGUE_ID = 1111;
    private static final int DIALOGUE_ID2 = 1112;
    private static final int SSN = 8;

    @Mock
    private BellSamTcapListener mockListener;
    @Mock
    private JainTcapProvider mockProvider;

    @Before
    public void setup() {
        objectToTest.clearAllDialogs();
    }

    @Test
    public void shouldActivateDialogue() throws Exception {
        final Dialogue dialogue = newDialogue(DIALOGUE_ID);
        objectToTest.activate(dialogue);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(dialogue));
    }

    @Test
    public void shouldDeActivateDialogue() throws Exception {
        final Dialogue dialogue = newDialogue(DIALOGUE_ID);
        objectToTest.activate(dialogue);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(dialogue));
        objectToTest.deactivate(dialogue);
        assertTrue(!objectToTest.isDialogueLeft());
    }

    @Test
    public void shouldClearAllDialogue() throws Exception {
        final Dialogue dialogue = newDialogue(DIALOGUE_ID);
        objectToTest.activate(dialogue);
        final Dialogue dialogue2 = newDialogue(DIALOGUE_ID2);
        objectToTest.activate(dialogue2);
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID), sameInstance(dialogue));
        assertThat(objectToTest.lookUpDialogue(DIALOGUE_ID2), sameInstance(dialogue2));
        assertTrue(objectToTest.isDialogueLeft());
        objectToTest.clearAllDialogs();
        assertTrue(!objectToTest.isDialogueLeft());
    }

    @Test(expected = DialogueExistsException.class)
    public void shouldThrowExceptionIfDialogueExists() throws Exception {
        final Dialogue dialogue = newDialogue(DIALOGUE_ID);
        objectToTest.activate(dialogue);
        objectToTest.activate(dialogue);
    }

    @Test(expected = NoDialogueExistsException.class)
    public void shouldThrowExceptionIfNoDialogueExists() throws Exception {
        final Dialogue dialogue = newDialogue(DIALOGUE_ID);
        objectToTest.deactivate(dialogue);
    }

    private Dialogue newDialogue(final int dialogueId) {
        Dialogue dialogue = new Dialogue(mockListener, dialogueId, SSN, mockProvider);
        return dialogue;
    }
}
