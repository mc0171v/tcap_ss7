package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStartState implements ITestState {
    
    private static final Logger logger = LoggerFactory.getLogger(TestStartState.class);

    private ITestDialogue dialogue;

    public TestStartState(final ITestDialogue dialogue) {
        super();
        this.dialogue = dialogue;
        logger.debug("State is TestStartState");
    }

    public ITestDialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(final ITestDialogue dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public void handleEvent(final IEvent event) {
        dialogue.setResult(event.getResult());
        dialogue.transition(new TestEndState(dialogue));
        logger.debug("Changed state to TestEndState");
    }
    
    @Override
    public void handleEventNoLatch(final IEvent event) {
        dialogue.setResultWOLatch(event.getResult());
        dialogue.transition(new TestEndState(dialogue));
        logger.debug("Changed state to TestEndState");
    }

}
