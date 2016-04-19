package com.vennetics.bell.sam.ss7.tcap.common.map;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ericsson.ein.ss7.commonparts.util.Tools;

public final class LocationHelper {
    
    private LocationHelper() {
        
    }
    
    private static final Logger logger = LoggerFactory.getLogger(LocationHelper.class);
    
    /**
     * GSM 03.32
     * N <= X*(2**23)/90 < N+1 latitude - X Degrees
     * MSB Sign
     * @param latitude
     * @return
     */
    public static Bounds getLatitude(final byte[] latitudeBytes) {
        final Bounds bounds = new Bounds();
        final int latitude = convert(latitudeBytes, true);
        final double denominator = Math.pow(2, 23);
        bounds.setLowerBound(getSign(latitudeBytes) * latitude * 90.0 / denominator);
        bounds.setUpperBound(getSign(latitudeBytes) * (latitude + 1) * 90.0 / denominator);
        logger.debug("Latitude: " + bounds.toString());
        return bounds;
    }

    /**
     * GSM 03.32
     * N <= X*(2**24)/360 < N+1 longitude -180 to +180 - X Degrees
     * Two's complement
     * @param longitude
     * @return
     */
    public static Bounds getLongitude(final byte[] longitudeBytes) {
        final Bounds bounds = new Bounds();
        final BigInteger longitude = convertTwosComplement(longitudeBytes);
        final double denominator = Math.pow(2, 24);
        bounds.setLowerBound(longitude.intValue() * 360.0 / denominator);
        bounds.setUpperBound((longitude.intValue() + 1) * 360.0 / denominator);
        logger.debug("Longitude: " + bounds.toString());
        return bounds;
    }

    /**
     * GSM 03.32
     * r = C*((1+X)**K - 1) Uncertainty
     *  (C=10, X=0.1)
     * @param uncertainty
     * @return uncertainty in meters
     */
    public static double getUncertainty(final byte uncertaintyByte) {
        final int power = uncertaintyByte & 0x7F;
        final double uncertainty = 10 * (Math.pow(1.1, power) - 1);
        logger.debug("Uncertainty: " + uncertainty);
        return uncertainty;
    }
    

    private static int convert(final byte[] latitude, final boolean signIncluded) {
        byte octet0 = latitude[0];
        if (signIncluded) {
            octet0 = Tools.getLoByteOf4(latitude[0] & 0x7F);
        }
        return ((octet0 & 0xFF) << 16) + ((latitude[1] & 0xFF) << 8) + (latitude[2] & 0xFF);
    }
    
    private static BigInteger convertTwosComplement(final byte[] longitude) {
        return new BigInteger(longitude);
    }
    
    private static int getSign(final byte[] latitude) {
        byte octet0 = latitude[0];
        final int sign = (octet0 & 0x80) >> 7 > 0 ? -1 : 1;
        return sign;
    }
}
