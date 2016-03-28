package com.vennetics.bell.sam.ss7.tcap.ati.simulator.dialogue.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.AbstractDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IInitialDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedResultException;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;
import com.vennetics.bell.sam.ss7.tcap.common.utils.TagLengthValue;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;

import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

@Component
public class AtiSimDialogueStart extends AbstractDialogueState implements IInitialDialogueState, Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(AtiSimDialogueStart.class);

    public static final byte[] ATI  = { 0x47 };
    
    private static String stateName = "AtiDialogueStart";

    public AtiSimDialogueStart(final IDialogueContext context, final IDialogue dialogue) {
        super(context, dialogue);
        logger.debug("Changing state to {}", getStateName());
    }
    
    public AtiSimDialogueStart() {
        super();
        logger.debug("Changing state to {}", getStateName());
    }

    public void activate() {
    }

    @Override
    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("ComponentIndEvent event received in state {}", getStateName());
        processComponentIndEvent(event);
    }

    @Override
    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("DialogueIndEvent event received in state {}", getStateName());
        processDialogueIndEvent(event);
    }

    /**
     *
     * @param event
     *
     * @exception SS7Exception
     */
    @Override
    public void processInvokeIndEvent(final InvokeIndEvent event) throws SS7Exception {
        logger.debug("InvokeIndEvent event received in state {}", getStateName());
        byte[] operation = event.getOperation().getOperationCode();
        final int dialogueId = event.getDialogueId();
        ResultReqEvent resultReq;
        if (operation[0] == ATI[0]) {
            resultReq = getDialogue().getComponentRequestBuilder()
                                     .createResultReq(getContext(),
                                                      dialogueId,
                                                      event.getInvokeId());

            resultReq.setInvokeId(event.getInvokeId());

            logger.debug("Sending result...");
            try {
                getDialogue().getJainTcapProvider().sendComponentReqEventNB(resultReq);
            } catch (Exception ex) {
                logger.error("Failed to send component");
            }

            // ---Build end request, depending on operation code

            EndReqEvent dialogueReq = getDialogue().getDialogueRequestBuilder()
                                                   .createEndReq(getContext(), dialogueId);
            try {
                getDialogue().getJainTcapProvider().sendDialogueReqEventNB(dialogueReq);
            } catch (Exception ex) {
                logger.error("Failed to send component");
            }
            logger.debug(event.toString());
            logger.debug("Changing state from {}", getStateName());
            terminate();
        }
    }

    byte[] getValueFromSequence(final byte[] bytes) {
        final List<TagLengthValue> tlvs = EncodingHelper.getTlvs(bytes);
        if (!(tlvs.size() == 1 && tlvs.get(0).getTag() == EncodingHelper.SEQUENCE_TAG)) {
            logger.error("Expecting sequence");
            throw new UnexpectedResultException(EncodingHelper.bytesToHex(bytes));
        }
        return tlvs.get(0).getValue();
    }
    
    /**
     * Dialogue event.
     */
    public void processEndIndEvent(final EndIndEvent event) throws SS7Exception {
        logger.debug("Expected EndIndEvent received.");
    }

    public String getStateName() {
        return stateName;
    }
    
    public void terminate() {
        getDialogue().setState(new AtiSimDialogueEnd(getContext(), getDialogue()));
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        if (getContext() == null && getDialogue() == null) {
          return super.clone();
        }
        throw new CloneNotSupportedException();
    }
}
