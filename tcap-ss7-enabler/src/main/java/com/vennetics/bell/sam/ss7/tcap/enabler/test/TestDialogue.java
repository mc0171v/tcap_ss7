package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDialogue implements ITestDialogue {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDialogue.class);
    
    private ITestState state;
    
    private IResult result;
    
    private CountDownLatch latch;

    public void setLatch(final CountDownLatch latch) {
        this.latch = latch;
    }
    
    public CountDownLatch getLatch() {
        return latch;
    }

    public IResult getResult() {
        return result;
    }

    public void setResult(final IResult result) {
        this.result = result;
        logger.debug("Counted down latch");
        latch.countDown();
    }
    
    public void setResultWOLatch(final IResult result) {
        this.result = result;
    }

    public ITestState getState() {
        return state;
    }

    public void setState(final ITestState state) {
        this.state = state;
    }

    public TestDialogue(final ITestState state) {
        super();
        this.state = state;
    }
    
    public TestDialogue() {
        super();
    }
    
    public void transition(final ITestState newState) {
        state = newState;
    }
    
    public void handleEvent(final IEvent event) {
        state.handleEvent(event);
    }

    public void handleEventWOLatch(final IEvent event) {
        state.handleEventNoLatch(event);
    }
}
