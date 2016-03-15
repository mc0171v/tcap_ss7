package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ericsson.einss7.japi.OutOfServiceException;
import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.WouldBlockException;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.BadProtocolException;
import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;

@Component
public class AtiDialogueStart extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AtiDialogueStart.class);

    private static final long BB_TEST_INVOKE_TIMEOUT = 5000;

    private static String stateName = "AtiDialogueStart";

    public AtiDialogueStart(final IDialogueContext context, final IDialogue dialogue) {
        super(context, dialogue);
        logger.debug("Changing state to {}", getStateName());
    }
    
    public AtiDialogueStart() {
        super();
        logger.debug("Changing state to {}", getStateName());
    }

    public void activate() {
        startDialogue();
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

    public void startDialogue() {
        EventObject currentReq = null;
        InvokeReqEvent invokeReq = null;
        BeginReqEvent beginReq = null;
        int dialogueId = -1;
        try {
            JainTcapProvider provider = getContext().getProvider();
            dialogueId = provider.getNewDialogueId(getContext().getSsn());
            getDialogue().setDialogueId(dialogueId);
            int invokeId = provider.getNewInvokeId(dialogueId);
            logger.debug("Starting dialogue with dialogueId:{} and invokeId:{}",
                         dialogueId,
                         invokeId);
            invokeReq = getDialogue().getComponentRequestBuilder().createInvokeReq(getContext().getTcapEventListener(),
                                                                invokeId,
                                                                getDialogue().getRequest(),
                                                                true);
            currentReq = invokeReq;
            invokeReq.setDialogueId(dialogueId);
            invokeReq.setTimeOut(BB_TEST_INVOKE_TIMEOUT);
            // set class 1 operation (report failure or success)
            invokeReq.setClassType(ComponentConstants.CLASS_1);
            provider.sendComponentReqEventNB(invokeReq);

            // ----- Build begin request:
            beginReq = getDialogue().getDialogueRequestBuilder().createBeginReq(getContext(), dialogueId);
            currentReq = beginReq;
            provider.sendDialogueReqEventNB(beginReq);
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

    /**
     *
     * @param event
     *
     * @exception SS7Exception
     */
    @Override
    public void processResultIndEvent(final ResultIndEvent event) throws SS7Exception {
        logger.debug("ResultIndEvent event received in state {}", getStateName());
        if (event.isParametersPresent()) {
            Parameters params = event.getParameters();
            final byte[] returnedBytes = params.getParameter();
            processReturnedBytes(returnedBytes);
        } else {
            OutboundATIMessage obm = (OutboundATIMessage) getDialogue().getRequest();
            obm.setStatus(99);
            getDialogue().setResult(obm);
        }
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
        logger.debug("Changing state from {}", getStateName());
        getDialogue().setState(new AtiDialogueEnd(getContext(), getDialogue()));
        getDialogue().activate();
    }

    private void processReturnedBytes(final byte[] returnedBytes) {
        logger.debug("processing returned bytes {}", returnedBytes);
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
}
