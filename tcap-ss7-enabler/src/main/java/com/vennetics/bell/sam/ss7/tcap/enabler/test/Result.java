package com.vennetics.bell.sam.ss7.tcap.enabler.test;

public class Result implements IResult {

    private String result;

    public Result(final String result) {
        super();
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }
    
    @Override
    public String toString() {
        return result;
    }
    
}
