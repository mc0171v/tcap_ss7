package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.OutOfServiceException;
import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.WouldBlockException;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.BadProtocolException;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueIndEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;

public class DialogueAnswer extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(DialogueAnswer.class);
    private static final byte[] OPERATION_CONTINUE_DIALOGUE = { 0x01 };
    private static final long BB_TEST_INVOKE_TIMEOUT = 5000;

    private static String stateName = "DialogueAnswer";

    public DialogueAnswer(final IDialogueContext context, final IDialogue dialogue) {
        super(context, dialogue);
        logger.debug("Changing state to {}", getStateName());
    }

    public void activate() {
        sendInvokeAndContinue(getDialogue().getDialogueId());
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
    public void processResultIndEvent(final ResultIndEvent event) throws SS7Exception {

        logger.debug("ResultIndEvent event received in state {}", getStateName());
        final JainTcapProvider provider = getContext().getProvider();
        final JainTcapStack stack = getContext().getStack();
        switch (stack.getProtocolVersion()) {
            case DialogueConstants.PROTOCOL_VERSION_ANSI_96:
                provider.releaseInvokeId(event.getLinkId(), event.getDialogueId());
                break;
            case DialogueConstants.PROTOCOL_VERSION_ITU_97:
                provider.releaseInvokeId(event.getInvokeId(), event.getDialogueId());
                break;
            default:
                throw new BadProtocolException("Wrong protocol version" + stack.getProtocolVersion());
        }
        IDialogue dialogue = getContext().getDialogue(event.getDialogueId());
        if (dialogue == null) {
            logger.error("No dialogue found");
            return;
        }
        logger.debug("Changing state from {}", getStateName());
        dialogue.setState(new DialogueEnd(getContext(), dialogue));
        dialogue.activate();
    }

    /**
     * Dialogue event.
     */
    public void processContinueIndEvent(final ContinueIndEvent event) throws SS7Exception {
        logger.debug("Expected Continue IndEvent received.");
    }

    /**
     * Build an invoke and continue request, then send them.
     *
     * @param dialogueId
     *            The dialogue to continue with.
     */
    private void sendInvokeAndContinue(final int dialogueId) {
        InvokeReqEvent invokeReq = null;
        ContinueReqEvent continueReq = null;
        EventObject currentReq = null;
        try {
            int invokeId = getContext().getProvider().getNewInvokeId(dialogueId);
            invokeReq = getDialogue().getComponentRequestBuilder().createInvokeReq(getContext().getTcapEventListener(),
                                                     invokeId,
                                                     OPERATION_CONTINUE_DIALOGUE,
                                                     true); // with paramters
            currentReq = invokeReq;
            invokeReq.setDialogueId(dialogueId);
            invokeReq.setTimeOut(BB_TEST_INVOKE_TIMEOUT);
            // set class 1 operation (report failure or success)
            invokeReq.setClassType(ComponentConstants.CLASS_1);
            logger.debug("Sending invoke with invokeID: " + invokeId + "  ...");

            getContext().getProvider().sendComponentReqEventNB(invokeReq);

            // ----- Build begin request:

            continueReq = getDialogue().getDialogueRequestBuilder().createContinueReq(getContext(), dialogueId);
            currentReq = continueReq;
            logger.debug("Sending continue...");
            getContext().getProvider().sendDialogueReqEventNB(continueReq);
        } catch (SS7Exception ex) {
            ex.printStackTrace();
        } catch (WouldBlockException vbEx) {
            handleWouldBlock(currentReq, vbEx);
        } catch (OutOfServiceException oosEx) {
            handleOutOfServiceException(currentReq, oosEx);
        } catch (VendorException vEx) {
            vEx.printStackTrace();
        }

    }

    public String getStateName() {
        return stateName;
    }
    
    public void terminate() {
        getDialogue().setState(new DialogueEnd(getContext(), getDialogue()));
    }
}
