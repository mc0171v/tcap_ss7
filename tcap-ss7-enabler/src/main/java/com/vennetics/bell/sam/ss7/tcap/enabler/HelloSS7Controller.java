package com.vennetics.bell.sam.ss7.tcap.enabler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states.DialogueAnswer;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.BellSamTcapListener;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IDialogue;

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
                Tools.getLoByteOf2(231),  //zone
                3,
                0
        };
        TcapUserAddress userAddressA1;
        TcapUserAddress userAddressB1;

        userAddressA1 = new TcapUserAddress(spcA, (short) 99);
        userAddressB1 = new TcapUserAddress(spcA, (short) 98);
        final BellSamTcapListener listener = new BellSamTcapListener(userAddressA1, userAddressB1);
        int retry =  0;
        while (!listener.isBound()  && retry < 10) {
            logger.debug("Waiting for bind {}", retry);
            retry++;
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                logger.debug("Caught exception");
            }

        }
        if (!listener.isBound()) {
        	listener.cleanup();
        	return "Did not bind";
        }
        logger.debug("User bound");
        retry =  0;
        while (!listener.isReady() && retry < 10) {
            logger.debug("Waiting for ready {}", retry);
        	retry++;
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                logger.debug("Caught exception");
            }

        }
        if (!listener.isReady()) {
        	listener.cleanup();
        	return "Is not ready";
        }
        logger.debug("User Ready");
        final IDialogue dialogue = listener.startDialogue();
        retry =  0;
        while (!dialogue.getStateName().equals("DialogueEnd") && retry < 10) {
            logger.debug("Waiting for end {} is {}", retry, dialogue.getStateName());
        	retry++;
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                logger.debug("Caught exception");
            }

        }
    	listener.cleanup();
        return "Started Dialogue";


    }

}
