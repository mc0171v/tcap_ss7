package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.OutOfServiceException;
import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.WouldBlockException;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IDialogue;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.BeginReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialogueConstants;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;


public class DialogueStarted extends AbstractDialogueState implements IDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(DialogueStarted.class);
    
    private static final int PARAM_LEN = Integer.parseInt(System.getProperty("PARAM_LEN", "30"));
    private static final long BB_TEST_INVOKE_TIMEOUT = 5000;
    private static final byte[] OPERATION_CONTINUE_DIALOGUE =  { 0x01};

    public DialogueStarted(final IDialogueContext context) {
        super(context);
        startDialogue();
    }

    @Override
    public void handleEvent(final ComponentIndEvent event) {
        logger.debug("Handling ComponentIndEvent in state DialogueStarted");
        processComponentIndEvent(event);
    }

    @Override
    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("Handling DialogueIndEvent in state DialogueStarted");
        processDialogueIndEvent(event);
    }
    
    public void startDialogue() {
        EventObject currentReq = null;
        InvokeReqEvent invokeReq = null;
        BeginReqEvent beginReq = null;
        int dialogueId = -1;
        try {
        	JainTcapProvider provider = context.getProvider();
            dialogueId = provider.getNewDialogueId(context.getSsn());
            int invokeId = provider.getNewInvokeId(dialogueId);
            logger.debug("Starting dialogue with dialogueId:{} and invokeId:{}",dialogueId,invokeId);
            invokeReq = createInvokeReq(this,
                                        invokeId,
                                        OPERATION_CONTINUE_DIALOGUE,
                                        true);
            currentReq = invokeReq;
            invokeReq.setDialogueId(dialogueId);
            invokeReq.setTimeOut(BB_TEST_INVOKE_TIMEOUT);
            // set class 1 operation (report failure or success)
            invokeReq.setClassType(ComponentConstants.CLASS_1);
            provider.sendComponentReqEventNB(invokeReq);

            // ----- Build begin request:
            beginReq = createBeginReq(this, dialogueId);
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
    
    public InvokeReqEvent createInvokeReq(final java.lang.Object source, final int invokeId, final byte[] opData) {
        return createInvokeReq(source, invokeId, opData, false);
    }

    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final byte[] opData,
                                          final boolean withparams) {

        return createInvokeReq(source, invokeId, opData, withparams, PARAM_LEN, false);
    }

    public InvokeReqEvent createInvokeReq(final java.lang.Object source,
                                          final int invokeId,
                                          final byte[] opData,
                                          final boolean withparams,
                                          final int paramLen,
                                          final boolean isRawParam) {

        final InvokeReqEvent invokeReq = new InvokeReqEvent(source, invokeId, null);
        invokeReq.setInvokeId(invokeId);
        invokeReq.setLastInvokeEvent(true);
        invokeReq.setTimeOut(100000);
        invokeReq.setClassType(ComponentConstants.CLASS_3);
        final Operation op = new Operation(); // FIX: Operation class, CODE to TYPE in
                                        // constants
        op.setOperationCode(opData);
        op.setPrivateOperationData(opData);
        op.setOperationFamily(Operation.OPERATIONFAMILY_PROCEDURAL);
        op.setOperationSpecifier(Operation.CHARGINGSPECIFIER_BILLCALL);
        op.setOperationType(Operation.OPERATIONTYPE_GLOBAL); // FIX: code vs.
                                                             // type
                                                             // missunderstanding
        invokeReq.setOperation(op);
        if (withparams) {
            final byte[] params = createParameters(paramLen, isRawParam);

            invokeReq.setParameters(new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, params));
        }
        return invokeReq;
    }

    protected byte[] createParameters(final int paramLength, final boolean isRaw) {

        final byte[] params = new byte[paramLength];
        int pos = 0;
        if (isRaw) {
            params[pos++] = Tools.getLoByteOf2(0x30); // sequence tag
            pos = setLength(paramLength, params, pos); // total length
        }
        params[pos++] = Tools.getLoByteOf2(0x04); // OCTETSTRING id (ASN.1)
        pos = setLength(paramLength - pos - 1, params, pos); // length of
                                                             // OCTETSTRING
        for (int i = pos; i < paramLength - pos; i++) {
            // fill up with dummy data
            params[i] = Tools.getLoByteOf2(i - pos);
        }
        return params;
    }

    protected int setLength(final int paramLength, final byte[] params, final int pos) {
        int currentPos = pos;
        if (paramLength <= 0x7F) {
            params[currentPos++] = Tools.getLoByteOf2(paramLength);
        } else if (paramLength <= 0xFF) {
            params[currentPos++] = Tools.getLoByteOf2(0x81); // length tag
            params[currentPos++] = Tools.getLoByteOf2(paramLength);
        } else {
            params[currentPos++] = Tools.getLoByteOf2(0x82); // length tag
            params[currentPos++] = Tools.getLoByteOf2(paramLength);
            params[currentPos++] = Tools.getHiByteOf2(paramLength);
        }
        return currentPos;
    }


    public BeginReqEvent createBeginReq(final java.lang.Object source,
                                        final int dialogueId) {

        final BeginReqEvent beginReq = new BeginReqEvent(source, dialogueId, context.getOrigAddr(), context.getDestAddr());
        beginReq.setQualityOfService((byte) 2); // FIX: constant for qos in JAIN
        beginReq.setOriginatingAddress(context.getOrigAddr());
        beginReq.setDestinationAddress(context.getDestAddr());
        beginReq.setDialoguePortion(new DialoguePortion());
        return beginReq;
    }
    
    /**
    *
    * @param event
    *
    * @exception SS7Exception
    */
    @Override
	public void processResultIndEvent(ResultIndEvent event) throws SS7Exception {

		logger.debug("ResultIndEvent received.");
		switch (context.getStack().getProtocolVersion()) {
		case DialogueConstants.PROTOCOL_VERSION_ANSI_96:
			context.getProvider().releaseInvokeId(event.getLinkId(), event.getDialogueId());
			break;
		case DialogueConstants.PROTOCOL_VERSION_ITU_97:
			context.getProvider().releaseInvokeId(event.getInvokeId(), event.getDialogueId());
			break;
		default:
			throw new RuntimeException("Wrong protocol version:\n");
		}
		IDialogue dialogue = context.getDialogueManager().lookUpDialogue(event.getDialogueId());
		if (dialogue == null) {
			logger.error("No dialogue found");
			return;
		}
		dialogue.setState(new DialogueAnswered(context));
	}
}
