package com.vennetics.bell.sam.ss7.tcap.enabler.test;

public class Event implements IEvent {

    private IResult result;

    public IResult getResult() {
        return result;
    }

    public void setResult(final IResult result) {
        this.result = result;
    }

    public Event(final IResult result) {
        super();
        this.result = result;
    }
}
