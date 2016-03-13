package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import java.util.concurrent.CountDownLatch;

public interface ITestDialogue {

    void transition(ITestState newState);
    
    void setResult(IResult result);

    void setResultWOLatch(IResult result);
    
    IResult getResult();
    
    void setState(ITestState state);
    
    void handleEvent(IEvent event);
    
    void handleEventWOLatch(IEvent event);
    
    void setLatch(CountDownLatch latch);
    
    CountDownLatch getLatch();
}
