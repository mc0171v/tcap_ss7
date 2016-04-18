package com.vennetics.bell.sam.ss7.tcap.common.map;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class LocationHelperTest {
        
    private static final byte[] TEST_LATITUDE_MIN = { 0x00, 0x00, 0x00 };
    private static final byte[] TEST_LATITUDE_MIN_NEG = { Tools.getLoByteOf4(0x80), 0x00, 0x00 };    
    private static final byte[] TEST_LATITUDE_MAX = { 0x7F, Tools.getLoByteOf4(0xFF), Tools.getLoByteOf4(0xFF) };
    private static final byte[] TEST_LATITUDE_MAX_NEG = { Tools.getLoByteOf4(0xFF), Tools.getLoByteOf4(0xFF), Tools.getLoByteOf4(0xFF) };
    private static final byte[] TEST_LONGITUDE_MIN = {  Tools.getLoByteOf4(0x80), 0x00, 0x00 };
    private static final byte[] TEST_LONGITUDE_MAX = { Tools.getLoByteOf4(0x7F), Tools.getLoByteOf4(0xFF), Tools.getLoByteOf4(0xFF) };
    
    private static final byte TEST_MIN_UNCERTAINTY = 0x00;
    private static final byte TEST_MID_UNCERTAINTY = 0x14;
    private static final byte TEST_MAX_UNCERTAINTY = 0x7F;
    
    private static final double MIN_UNCERTAINTY = 0;
    private static final double MID_UNCERTAINTY = 57.3;
    private static final double MAX_UNCERTAINTY = 1800000;

    private double delta = 1.0/Math.pow(2, 25);
    private double uncertaintyDelta = 0.01;
    private static double MIN_LATITUDE = 0;
    private static double MAX_LATITUDE =  Math.pow(2, 23) - 1;
    private static double MIN_LONGITUDE = - Math.pow(2, 23);
    private static double MAX_LONGITUDE = Math.pow(2, 23) - 1;
    
    @Test
    public void testGetLatitudeMin() {
        Bounds expectedBounds = new Bounds();
        expectedBounds.setLowerBound(MIN_LATITUDE);
        expectedBounds.setUpperBound(90.0 / Math.pow(2, 23));
        Bounds bounds = LocationHelper.getLatitude(TEST_LATITUDE_MIN);
        assertEquals(expectedBounds.getLowerBound(), bounds.getLowerBound(), delta);
        assertEquals(expectedBounds.getUpperBound(), bounds.getUpperBound(), delta);
    }
    
    @Test
    public void testGetLatitudeMax() {
        Bounds expectedBounds = new Bounds();
        expectedBounds.setLowerBound(90.0 * (MAX_LATITUDE) / Math.pow(2, 23));
        expectedBounds.setUpperBound(90.0 * (MAX_LATITUDE + 1) / Math.pow(2, 23));
        Bounds bounds = LocationHelper.getLatitude(TEST_LATITUDE_MAX);
        assertEquals(expectedBounds.getLowerBound(), bounds.getLowerBound(), delta);
        assertEquals(expectedBounds.getUpperBound(), bounds.getUpperBound(), delta);
    }
    
    @Test
    public void testGetLatitudeMinNeg() {
        Bounds expectedBounds = new Bounds();
        expectedBounds.setLowerBound(MIN_LATITUDE);
        expectedBounds.setUpperBound(- 90.0 / Math.pow(2, 23));
        Bounds bounds = LocationHelper.getLatitude(TEST_LATITUDE_MIN_NEG);
        assertEquals(expectedBounds.getLowerBound(), bounds.getLowerBound(), delta);
        assertEquals(expectedBounds.getUpperBound(), bounds.getUpperBound(), delta);
    }
    
    @Test
    public void testGetLatitudeMaxNeg() {
        Bounds expectedBounds = new Bounds();
        expectedBounds.setLowerBound(- 90.0 * (MAX_LATITUDE) / Math.pow(2, 23));
        expectedBounds.setUpperBound(- 90.0 * (MAX_LATITUDE + 1) / Math.pow(2, 23));
        Bounds bounds = LocationHelper.getLatitude(TEST_LATITUDE_MAX_NEG);
        assertEquals(expectedBounds.getLowerBound(), bounds.getLowerBound(), delta);
        assertEquals(expectedBounds.getUpperBound(), bounds.getUpperBound(), delta);
    }
    
    @Test
    public void testGetLongtudeMin() {
        Bounds expectedBounds = new Bounds();
        expectedBounds.setLowerBound(360.0 * MIN_LONGITUDE/Math.pow(2, 24));
        expectedBounds.setUpperBound((360.0 * (MIN_LONGITUDE+ 1.0))/Math.pow(2, 24));
        Bounds bounds = LocationHelper.getLongitude(TEST_LONGITUDE_MIN);
        assertEquals(expectedBounds.getLowerBound(), bounds.getLowerBound(), delta);
        assertEquals(expectedBounds.getUpperBound(), bounds.getUpperBound(), delta);
    }
    
    @Test
    public void testGetLongtudeMax() {
        Bounds expectedBounds = new Bounds();
        expectedBounds.setLowerBound(360.0 * MAX_LONGITUDE/Math.pow(2, 24));
        expectedBounds.setUpperBound((360.0 * (MAX_LONGITUDE + 1))/Math.pow(2, 24));
        Bounds bounds = LocationHelper.getLongitude(TEST_LONGITUDE_MAX);
        assertEquals(expectedBounds.getLowerBound(), bounds.getLowerBound(), delta);
        assertEquals(expectedBounds.getUpperBound(), bounds.getUpperBound(), delta);
    }
    
    @Test
    public void testUncertainty() {
        double uncertainty = LocationHelper.getUncertainty(TEST_MIN_UNCERTAINTY);
        assertEquals(MIN_UNCERTAINTY, uncertainty, uncertaintyDelta);
        uncertainty = LocationHelper.getUncertainty(TEST_MID_UNCERTAINTY);
        assertEquals(MID_UNCERTAINTY, uncertainty, uncertaintyDelta * uncertainty);
        uncertainty = LocationHelper.getUncertainty(TEST_MAX_UNCERTAINTY);
        assertEquals(MAX_UNCERTAINTY, uncertainty, uncertaintyDelta * uncertainty);
    }
}
