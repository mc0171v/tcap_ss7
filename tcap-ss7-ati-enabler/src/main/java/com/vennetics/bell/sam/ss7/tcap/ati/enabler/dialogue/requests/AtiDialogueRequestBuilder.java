package com.vennetics.bell.sam.ss7.tcap.ati.enabler.dialogue.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.AbstractDialogueRequestBuilder;

import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;

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
            0x03         /* version 3 */
          };
    
    private static final String STATE_TYPE = "ATI";
    
    AtiDialogueRequestBuilder() {
        super(STATE_TYPE);
        logger.debug("Constructed DialogueRequestBuilder");
    }
    
    /*
     * (non-Javadoc)
     * @see
     * com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.AbstractDialogueRequestBuilder#createBeginReq(com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext,
     *                                                                                                             int)
     */
    @Override
    public BeginReqEvent createBeginReq(final IDialogueContext context, final int dialogueId) {

        final BeginReqEvent beginReq = new BeginReqEvent(context.getTcapEventListener(),
                                                         dialogueId,
                                                         context.getOrigAddr(),
                                                         context.getDestAddr());
        beginReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        final DialoguePortion dialoguePortion = new DialoguePortion();
        dialoguePortion.setAppContextName(ANYTIMEINFOENQUIRYCONTEXT_V3);
        beginReq.setDialoguePortion(dialoguePortion);
        return beginReq;
    }
}
