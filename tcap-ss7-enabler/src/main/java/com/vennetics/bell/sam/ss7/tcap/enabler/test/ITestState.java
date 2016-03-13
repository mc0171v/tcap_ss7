package com.vennetics.bell.sam.ss7.tcap.enabler.test;

public interface ITestState {

    void handleEvent(IEvent event);

    void handleEventNoLatch(IEvent event);
}
