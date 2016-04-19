package com.vennetics.bell.sam.ss7.tcap.common.map;

public class Bounds {
    
    private double upperBound;
    
    private double lowerBound;

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(final double upperBound) {
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(final double lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    @Override
    public String toString() {
        return "Upper Bound: " + upperBound + ", Lower Bound: " + lowerBound;
    }

}
