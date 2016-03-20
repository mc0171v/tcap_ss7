package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ericsson.jain.protocol.ss7.tcap.Tools;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedResultException;
import com.vennetics.bell.sam.ss7.tcap.enabler.utils.EncodingHelper;
import com.vennetics.bell.sam.ss7.tcap.enabler.utils.TagLengthValue;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.DialogueReqEvent;

import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndReqEvent;

@Component
public class AtiSimDialogueStart extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AtiSimDialogueStart.class);

    public static final byte[] ATI  = { 0x47};
    
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
            resultReq = createResultReq(getContext(),
                                        dialogueId,
                                        event.getInvokeId(),
                                        true);

        resultReq.setInvokeId(event.getInvokeId());

        logger.debug("Sending result...");
        try {
            getDialogue().getJainTcapProvider().sendComponentReqEventNB(resultReq);
        } catch (Exception ex) {
            logger.error("Failed to send component");
        }

        //---Build end request, depending on operation code

        DialogueReqEvent dialogueReq = createEndReq(getContext(), dialogueId);
        try {
            getDialogue().getJainTcapProvider().sendDialogueReqEventNB(dialogueReq);
        } catch (Exception ex) {
            logger.error("Failed to send component");
        }
        logger.debug(event.toString());
        logger.debug("Changing state from {}", getStateName());
        getDialogue().setState(new AtiSimDialogueEnd(getContext(), getDialogue()));
        getDialogue().activate();
        }
    }

    private DialogueReqEvent createEndReq(final IDialogueContext context, final int dialogueId) {
            EndReqEvent endReq = new EndReqEvent(context, 0);
            endReq.setDialogueId(dialogueId);
            endReq.setQualityOfService((byte) 2);
            endReq.setTermination(DialogueConstants.TC_BASIC_END);
            endReq.setDialoguePortion(new DialoguePortion());

            return endReq;
    }

    private ResultReqEvent createResultReq(final IDialogueContext context,
                                           final int dialogueId,
                                           final int invokeId,
                                           final boolean b) {
        ResultReqEvent resReq = new ResultReqEvent(context, dialogueId, true);
        resReq.setInvokeId(invokeId);
        final Operation op = new Operation();
        op.setOperationCode(ATI);
        op.setOperationType(Operation.OPERATIONTYPE_LOCAL);
        resReq.setOperation(op);
        //final byte[] bytes = {0x30, 0x06, 0x30, 0x04, Tools.getLoByteOf2(0xA1), 0x02, Tools.getLoByteOf2(0x80), 0x00 };
        final byte[] bytes = { 0x30, 0x12, 0x30, 0x10, Tools.getLoByteOf2(0xA0), 0x0E, 0x02, 0x02, 0x01, 0x03, Tools.getLoByteOf2(0x80),
                0x08, 0x10, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x01 };
        Parameters params = new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, bytes);
        resReq.setParameters(params);
        return resReq;
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
        getDialogue().setState(new AtiDialogueEnd(getContext(), getDialogue()));
    }
}
