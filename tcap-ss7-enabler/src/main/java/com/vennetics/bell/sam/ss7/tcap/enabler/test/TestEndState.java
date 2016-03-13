package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEndState implements ITestState {
    
    private static final Logger logger = LoggerFactory.getLogger(TestEndState.class);


    private ITestDialogue dialogue;
    
    public TestEndState(final ITestDialogue dialogue) {
        super();
        this.dialogue = dialogue;
        logger.debug("State is TestEndState");
    }

    public ITestDialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(final ITestDialogue dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public void handleEvent(final IEvent event) {
        logger.debug("Unexpected event {}", event);
    }
    
    @Override
    public void handleEventNoLatch(final IEvent event) {
        logger.debug("Unexpected event {}", event);
    }

}
