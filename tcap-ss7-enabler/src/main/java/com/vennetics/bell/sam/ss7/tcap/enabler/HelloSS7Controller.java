package com.vennetics.bell.sam.ss7.tcap.enabler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vennetics.bell.sam.ss7.tcap.enabler.service.BellSamTcapListener;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.TcapUserAddress;

@RestController
@RefreshScope
@EnableAutoConfiguration
public class HelloSS7Controller {

    private static final Logger logger = LoggerFactory.getLogger(HelloSS7Controller.class);

    @RequestMapping("/helloss7")
    public String message() {

        final byte[] spcA = { // signaling point 2143
                0, // id
                3, // area
                Tools.getLoByteOf2(231), // zone
        };
        TcapUserAddress userAddressA1;
        TcapUserAddress userAddressB1;

        userAddressA1 = new TcapUserAddress(spcA, (short) 99);
        userAddressB1 = new TcapUserAddress(spcA, (short) 98);
        final BellSamTcapListener listener = new BellSamTcapListener(userAddressA1, userAddressB1);
        while (!listener.isBound()) {
            logger.debug("Waiting for bind");
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                logger.debug("Caught exception");
            }

        }
        return "Stack Bound";
    }

}
