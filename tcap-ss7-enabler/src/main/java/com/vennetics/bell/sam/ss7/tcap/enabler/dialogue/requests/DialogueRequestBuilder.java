package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class DialogueRequestBuilder extends AbstractDialogueRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DialogueRequestBuilder.class);
                                                                 
    DialogueRequestBuilder() {
        logger.debug("Constructed DialogueRequestBuilder");
    }
    
}
