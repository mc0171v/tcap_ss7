package com.vennetics.bell.sam.ss7.tcap.ati.simulator.dialogue.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.AbstractDialogueRequestBuilder;

import jain.protocol.ss7.tcap.dialogue.DialogueConstants;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

@Component
public class AtiDialogueRequestBuilder extends AbstractDialogueRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AtiDialogueRequestBuilder.class);

    private static final byte[] ANYTIMEINFOENQUIRYCONTEXT_V3 = {
            0x06,        /* object identifier */
            0x07,        /* length */
            0x04,        /* CCITT */
            0x00,        /* ETSI */
            0x00,        /* Mobile domain */
            0x01,        /* GSM network */
            0x00,        /* application contexts */
            0x29,        /* anyTimeEnquiry */
            0x03        /* version 3 */
          };
    
    private static final String STATE_TYPE = "ATI";
    
    AtiDialogueRequestBuilder() {
        super(STATE_TYPE);
        logger.debug("Constructed DialogueRequestBuilder");
    }
   
    @Override
    public EndReqEvent createEndReq(final IDialogueContext context, final int dialogueId) {
            EndReqEvent endReq = new EndReqEvent(context.getTcapEventListener(), 0);
            endReq.setDialogueId(dialogueId);
            endReq.setQualityOfService((byte) 2);
            endReq.setTermination(DialogueConstants.TC_BASIC_END);
            final DialoguePortion dialoguePortion = new DialoguePortion();
            dialoguePortion.setAppContextName(ANYTIMEINFOENQUIRYCONTEXT_V3);
            endReq.setDialoguePortion(dialoguePortion);

            return endReq;
    }
}
