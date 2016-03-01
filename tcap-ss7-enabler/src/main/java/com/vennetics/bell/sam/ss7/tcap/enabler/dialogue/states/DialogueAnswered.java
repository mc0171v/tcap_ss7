package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.OutOfServiceException;
import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.WouldBlockException;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.enabler.service.IDialogueContext;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.DialogueReqEvent;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.ResultReqEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueReqEvent;
import jain.protocol.ss7.tcap.dialogue.DialoguePortion;

public class DialogueAnswered extends AbstractDialogueState implements IDialogueState {
    
    private static final Logger logger = LoggerFactory.getLogger(DialogueAnswered.class);
    
    private static final byte[] OPERATION_CONTINUE_DIALOGUE =  { 0x01};
    
    public DialogueAnswered(final IDialogueContext context) {
        super(context);
    }

    @Override
    public void handleEvent(final ComponentIndEvent event) {
    	logger.debug("Handling ComponentIndEvent in state DialogueAnswered");
        processComponentIndEvent(event);
    }

    @Override
    public void handleEvent(final DialogueIndEvent event) {
        logger.debug("Handling DialogueIndEvent in state DialogueAnswered");
        processDialogueIndEvent(event);
    }


    public ResultReqEvent createResultReq(final java.lang.Object source,
                                          final int dialogId,
                                          final int invokeId,
                                          final boolean lastResult) {

        ResultReqEvent resReq = new ResultReqEvent(source, dialogId, lastResult);
        resReq.setInvokeId(invokeId);
        return resReq;
    }
    
    public void processInvokeIndEvent(InvokeIndEvent event)
    throws SS7Exception, VendorException {

        EventObject currentReq = null;
        int dialogueId = -1;
        try {
            logger.debug("InvokeIndEvent received.");
            InvokeIndEvent invokeInd = (InvokeIndEvent)event;

            //----- Build result request:
            dialogueId = invokeInd.getDialogueId();
            ResultReqEvent resultReq = null;
            currentReq = resultReq = createResultReq(this,
                                                     dialogueId,
                                                     event.getInvokeId(),
                                                     true);
            resultReq.setInvokeId(invokeInd.getInvokeId());


            logger.debug("Sending result...");
            context.getProvider().sendComponentReqEventNB(resultReq);

            //---Build continue or end request, depending on operation code

            DialogueReqEvent dialogueReq = null;
            byte[] operation = event.getOperation().getOperationCode();

            if (operation[0] == OPERATION_CONTINUE_DIALOGUE[0]) {
                currentReq = dialogueReq =
                    createContinueReq(this, dialogueId);
            } else {
            	throw new UnexpectedPrimitiveException(event.getPrimitiveType());
            }
            logger.debug("Sending continue or end...");
            context.getProvider().sendDialogueReqEventNB(dialogueReq);
        } catch (WouldBlockException vbEx) {
            handleWouldBlock(currentReq, vbEx);
        } catch (OutOfServiceException oosEx) {
            handleOutOfServiceException(currentReq, oosEx);
        } catch (VendorException vEx) {
            vEx.printStackTrace();
            throw vEx;
        }
    }

    public ContinueReqEvent createContinueReq (java.lang.Object source,
                                               int dialogueId ) {

        ContinueReqEvent endReq = new ContinueReqEvent(source,0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte)2); //FIX: constant for qos in JAIN
        endReq.setDialoguePortion(new DialoguePortion());
        return endReq;
    }
}
