package com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig;


import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties.Ss7Address;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

import ericsson.ein.ss7.commonparts.util.Tools;

@RunWith(MockitoJUnitRunner.class)
public class Ss7ConfigurationPropertiesTest {
    
    private static final long LONG_DPC_255 = 255;
    private static final long LONG_DPC_256 = 256;
    private static final long LONG_DPC_67500 = 67500;
    private static final byte[] DPC_255 = { Tools.getLoByteOf2(0xFF), 0x00, 0x00 };
    private static final byte[] DPC_256 = { Tools.getLoByteOf2(0x00), 0x01, 0x00 };
    private static final byte[] DPC_67500 = { Tools.getLoByteOf2(0xAC), 0x07, 0x01 };

    @Test
    public void testDecodeOfSingleSequenceWithMultipleElements() {}
    
    @Test
    public void testSpcEncodingOneOctet() {
        final Ss7Address address1 = new Ss7Address();
        address1.setSpc(LONG_DPC_255);
        System.out.println(EncodingHelper.bytesToHex(address1.getSpc()));
        assertArrayEquals(address1.getSpc(), DPC_255);
    }
    
    @Test
    public void testSpcEncodingTwoOctet() {
        final Ss7Address address1 = new Ss7Address();
        address1.setSpc(LONG_DPC_256);
        System.out.println(EncodingHelper.bytesToHex(address1.getSpc()));
        assertArrayEquals(address1.getSpc(), DPC_256);
    }
    
    @Test
    public void testSpcEncodingThreeOctet() {
        final Ss7Address address1 = new Ss7Address();
        address1.setSpc(LONG_DPC_67500);
        System.out.println(EncodingHelper.bytesToHex(address1.getSpc()));
        assertArrayEquals(address1.getSpc(), DPC_67500);
    }

}
