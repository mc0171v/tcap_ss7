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

import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.ParameterNotSetException;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

@Component
public class AtiSimDialogueStart extends AbstractDialogueState implements IInitialDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AtiSimDialogueStart.class);

    private static final byte[] ATI  = { 0x47 };
    
    private static String stateName = "AtiDialogueStart";

    public AtiSimDialogueStart(final IDialogue dialogue) {
        super(dialogue);
        logger.debug("Changing state to {}", getStateName());
    }
    
    public AtiSimDialogueStart() {
        super();
        logger.debug("Changing state to {}", getStateName());
    }

    @Override
    public void activate() {
        logger.debug("Nothing to do");
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
    public void processInvokeIndEvent(final InvokeIndEvent event) {
        logger.debug("InvokeIndEvent event received in state {}", getStateName());
        int dialogueId;
        int invokeId;
        byte[] operation;
        try {
            operation = event.getOperation().getOperationCode();
            dialogueId = event.getDialogueId();
            invokeId = event.getInvokeId();
        } catch (ParameterNotSetException ex) {
            logger.error("Could not extract parameters {}", ex);
            terminate();
            return;
        }
        ResultReqEvent resultReq;
        final IDialogueContext context = getDialogue().getContext();
        if (operation[0] == ATI[0]) {
            resultReq = context.getComponentRequestBuilder()
                                     .createResultReq(context.getTcapEventListener(),
                                                      dialogueId,
                                                      invokeId);
            logger.debug("Sending result...");
            try {
                context.getProvider().sendComponentReqEventNB(resultReq);
            } catch (Exception ex) {
                logger.error("Failed to send component {}", ex);
                terminate();
                return;
            }

            // ---Build end request, depending on operation code

            EndReqEvent dialogueReq = context.getDialogueRequestBuilder()
                                                   .createEndReq(context, dialogueId);
            try {
                context.getProvider().sendDialogueReqEventNB(dialogueReq);
            } catch (Exception ex) {
                logger.error("Failed to send component {}", ex);
                terminate();
                return;
            }
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
    @Override
    public void processEndIndEvent(final EndIndEvent event) {
        logger.debug("Expected EndIndEvent received.");
    }

    @Override
    public String getStateName() {
        return stateName;
    }
    
    @Override
    public void terminate() {
        getDialogue().setState(new AtiSimDialogueEnd(getDialogue()));
    }
    
    @Override
    public IInitialDialogueState newInstance() {
          return new AtiSimDialogueStart();
  
    }
}
