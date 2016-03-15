package com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.states;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.OutOfServiceException;
import com.ericsson.einss7.japi.VendorDialogueReqEvent;
import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.WouldBlockException;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.enabler.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.BellSamOutOfServiceException;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.DialogueExtractionException;
import com.vennetics.bell.sam.ss7.tcap.enabler.exception.UnexpectedPrimitiveException;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.ComponentReqEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.DialogueReqEvent;
import jain.protocol.ss7.tcap.TcapConstants;
import jain.protocol.ss7.tcap.component.ErrorIndEvent;
import jain.protocol.ss7.tcap.component.InvokeIndEvent;
import jain.protocol.ss7.tcap.component.LocalCancelIndEvent;
import jain.protocol.ss7.tcap.component.RejectIndEvent;
import jain.protocol.ss7.tcap.component.ResultIndEvent;
import jain.protocol.ss7.tcap.dialogue.BeginIndEvent;
import jain.protocol.ss7.tcap.dialogue.ContinueIndEvent;
import jain.protocol.ss7.tcap.dialogue.EndIndEvent;
import jain.protocol.ss7.tcap.dialogue.NoticeIndEvent;
import jain.protocol.ss7.tcap.dialogue.ProviderAbortIndEvent;

public abstract class AbstractDialogueState {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDialogueState.class);

    private IDialogueContext context;

    private IDialogue dialogue;

    public AbstractDialogueState(final IDialogueContext context, final IDialogue dialogue) {
        this.context = context;
        this.dialogue = dialogue;
    }
    
    public AbstractDialogueState() {
    }

    public IDialogueContext getContext() {
        return context;
    }

    public void setContext(final IDialogueContext context) {
        this.context = context;
    }

    public IDialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(final IDialogue dialogue) {
        this.dialogue = dialogue;
    }

    public abstract void handleEvent(final ComponentIndEvent event);

    public abstract void handleEvent(final DialogueIndEvent event);

    /**
     *
     * @param invokeReq
     * @param dialogueReq
     * @param vbEx
     */
    protected void handleWouldBlock(final EventObject currentReq, final WouldBlockException vbEx) {
        final int dialogueId = getDialogueId(currentReq);
        context.getDialogueManager()
               .deactivate(dialogue);
        // release dialogueId
        context.getProvider().releaseDialogueId(dialogueId);

        context.startDialogue(dialogue.getRequest(),
                              dialogue.getLatch());
    }

    /**
     *
     * @param invokeReq
     * @param dialogueReq
     * @param hdEx
     */
    protected void handleOutOfServiceException(final EventObject currentReq,
                                               final OutOfServiceException hdEx) {

        // release dialogue
        int dialogueId = -1;
        dialogueId = getDialogueId(currentReq);
        context.getDialogueManager()
               .deactivate(context.getDialogueManager().lookUpDialogue(dialogueId));
        // release dialogueId
        context.getProvider().releaseDialogueId(dialogueId);
        throw new BellSamOutOfServiceException(dialogueId);
    }

    private int getDialogueId(final EventObject currentReq) {
        // release dialogue
        int dialogueId;
        try {
            if (currentReq instanceof VendorDialogueReqEvent) {
                dialogueId = ((VendorDialogueReqEvent) currentReq).getDialogueId();
            } else if (currentReq instanceof ComponentReqEvent) {
                dialogueId = ((ComponentReqEvent) currentReq).getDialogueId();
            } else {
                dialogueId = ((DialogueReqEvent) currentReq).getDialogueId();
            }
        } catch (Exception exception) {
            throw new DialogueExtractionException(exception.getMessage());
        }

        return dialogueId;
    }

    /**
     * Dialogue Events dispatching.
     *
     * @param event
     */
    public void processDialogueIndEvent(final DialogueIndEvent event) {
        try {
            int primitive = event.getPrimitiveType();
            logger.debug("Received primitive {}", primitive);
            switch (primitive) {
                case TcapConstants.PRIMITIVE_BEGIN:
                    processBeginIndEvent((BeginIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_CONTINUE:
                    processContinueIndEvent((ContinueIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_END:
                    processEndIndEvent((EndIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_PROVIDER_ABORT:
                    processProviderAbortIndEvent((ProviderAbortIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_NOTICE:
                    processNoticeIndEvent((NoticeIndEvent) event);
                    break;
                default:
                    throw new UnexpectedPrimitiveException(event.getPrimitiveType());
            }
        } catch (SS7Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Dialogue event.
     */
    public void processBeginIndEvent(final BeginIndEvent event) throws SS7Exception {

        logger.debug("BeginIndEvent received.");
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processContinueIndEvent(final ContinueIndEvent event) throws SS7Exception {
        logger.debug("Continue IndEvent received.");
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processEndIndEvent(final EndIndEvent event) throws SS7Exception {
        logger.debug("EndIndEvent received.");
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processProviderAbortIndEvent(final ProviderAbortIndEvent event) throws SS7Exception {

        logger.debug("ProviderAbortIndEvent received.");
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processNoticeIndEvent(final NoticeIndEvent event) throws SS7Exception {

        logger.debug("processNoticeIndEvent");
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Component Events dispatching.
     *
     * @param event
     *            A specific component.
     */
    public void processComponentIndEvent(final ComponentIndEvent event) {

        try {
            int primitive = event.getPrimitiveType();
            logger.debug("Received primitive {}", primitive);
            switch (primitive) {
                case TcapConstants.PRIMITIVE_INVOKE:
                    processInvokeIndEvent((InvokeIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_RESULT:
                    processResultIndEvent((ResultIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_ERROR:
                    processErrorIndEvent((ErrorIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_LOCAL_CANCEL:
                    processLocalCancelIndEvent((LocalCancelIndEvent) event);
                    break;
                case TcapConstants.PRIMITIVE_REJECT:
                    processRejectIndEvent((RejectIndEvent) event);
                    break;
                default:
                    throw new UnexpectedPrimitiveException(primitive);            }
        } catch (Exception ex) {
            throw new UnexpectedPrimitiveException(event.getPrimitiveType());
        }
    }

    protected void processInvokeIndEvent(final InvokeIndEvent event) throws SS7Exception,
                                                                     VendorException {
        logger.debug("InvokeIndEvent event received");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    protected void processResultIndEvent(final ResultIndEvent event) throws SS7Exception {
        logger.debug("ResultIndEvent event received");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    protected void processErrorIndEvent(final ErrorIndEvent event) {
        logger.debug("ErrorIndEvent event received");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    protected void processLocalCancelIndEvent(final LocalCancelIndEvent event) {
        logger.debug("LocalCancelIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    protected void processRejectIndEvent(final RejectIndEvent event) {
        logger.debug("RejectIndEvent event received in state ListenerUnbound");

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }
}
