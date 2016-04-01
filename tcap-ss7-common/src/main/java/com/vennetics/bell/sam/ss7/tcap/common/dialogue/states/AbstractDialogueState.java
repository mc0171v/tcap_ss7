package com.vennetics.bell.sam.ss7.tcap.common.dialogue.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.einss7.japi.OutOfServiceException;
import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.WouldBlockException;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.UnexpectedPrimitiveException;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.ParameterNotSetException;
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
    private static final String FAILEDTOEXTRACT = "Failed to extract dialogueId or invokeId: {}";
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
     * @param vbEx
     */
    protected void handleWouldBlock(final WouldBlockException vbEx) {
        logger.debug("Received would block exception {} when starting dialogue, starting again", vbEx.getMessage());
        context.getDialogueManager()
               .deactivate(dialogue);
        // release dialogueId
        context.getProvider().releaseDialogueId(dialogue.getDialogueId());

        context.startDialogue(dialogue.getRequest(),
                              dialogue.getLatch());
        //TODO Possible infinite loop
    }

    /**
     * 
     * @param hdEx
     */
    protected void handleOutOfServiceException(final OutOfServiceException hdEx) {
        terminate();
        final String errorMessage = "Out of service: " + hdEx.getMessage();
        logger.error(errorMessage);
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }
    
    /**
     * 
     * @param hdEx
     */
    protected void handleSs7Exception(final SS7Exception hdEx) {
        terminate();
        final String errorMessage = "SS7 exception: " + hdEx.getMessage();
        logger.error(errorMessage);
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }
    
    /**
     * 
     * @param vEx
     */
    protected void handleVendorException(final VendorException vEx) {
        terminate();
        final String errorMessage = "SS7 Vendor exception: " + vEx.getMessage();
        logger.error(errorMessage);
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }

    /**
     * Dialogue Events dispatching.
     *
     * @param event
     */
    public void processDialogueIndEvent(final DialogueIndEvent event) {
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
    }

    /**
     * Dialogue event.
     */
    public void processBeginIndEvent(final BeginIndEvent event) {

        logger.debug("BeginIndEvent received in state {}", dialogue.getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processContinueIndEvent(final ContinueIndEvent event) {
        logger.debug("Continue IndEvent received in state {}", dialogue.getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processEndIndEvent(final EndIndEvent event) {
        logger.debug("EndIndEvent received in state {}", dialogue.getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Dialogue event.
     */
    public void processProviderAbortIndEvent(final ProviderAbortIndEvent event) {
        String errorMessage = "Provider abort received";
        try {
            errorMessage = errorMessage + ", abort reason is " + event.getPAbort();
            logger.debug("ProviderAbortIndEvent received in state {} for dialogue {}",
                     dialogue.getStateName(),
                     event.getDialogueId());
        } catch (ParameterNotSetException ex) {
            logger.error("Parameter not set exception {}", ex);
            logger.debug("ProviderAbortIndEvent received in state {}",
                         dialogue.getStateName());
        }
        logger.error(errorMessage);
        terminate();
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }

    /**
     * Dialogue event.
     */
    public void processNoticeIndEvent(final NoticeIndEvent event) {

        logger.debug("processNoticeIndEvent received in state {}", dialogue.getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Component Events dispatching.
     *
     * @param event
     *            A specific component.
     */
    public void processComponentIndEvent(final ComponentIndEvent event) {
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
                    logger.error("Cannot identify primitive {}", primitive);
                    }
    }

    protected void processInvokeIndEvent(final InvokeIndEvent event) {
        logger.debug("InvokeIndEvent event received in state {}", dialogue.getStateName());

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    protected void processResultIndEvent(final ResultIndEvent event) {
        logger.debug("ResultIndEvent event received in state {}", dialogue.getStateName());

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    protected void processErrorIndEvent(final ErrorIndEvent event) {
        try {
            logger.debug("ErrorIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                         dialogue.getStateName(),
                         event.getDialogueId(),
                         event.getInvokeId());
        } catch (Exception ex) {
            logger.error(FAILEDTOEXTRACT, ex);
            logger.debug("ErrorIndEvent event received in state {}",
                         dialogue.getStateName()); 
        }
        String errorMessage;
        try {
            errorMessage = "Error Type: " +  event.getErrorType() + " Code: " + EncodingHelper.bytesToHex(event.getErrorCode());
        } catch (Exception ex) {
            logger.error("Failed to identify error type or code: {}", ex);
            errorMessage = "Error Type: Unknown Code: Unknown";
        }
        logger.error(errorMessage);
        terminate();
        dialogue.setError(new Ss7ServiceException(errorMessage));

    }

    protected void processLocalCancelIndEvent(final LocalCancelIndEvent event) {
        try {
            logger.debug("LocalCancelIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                         dialogue.getStateName(),
                         event.getDialogueId(),
                         event.getInvokeId());
        } catch (Exception ex) {
            logger.error(FAILEDTOEXTRACT, ex);
            logger.debug("LocalCancelIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                         dialogue.getStateName());
        }
        final String errorMessage = "Local cancel returned";
        logger.error(errorMessage);
        terminate();
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }

    protected void processRejectIndEvent(final RejectIndEvent event) {
        try {
        logger.debug("RejectIndEvent event received in state {} for dialogueId: {}, invokeId: {}", 
                     dialogue.getStateName(),
                     event.getDialogueId(),
                     event.getInvokeId());
        } catch (Exception ex) {
            logger.error(FAILEDTOEXTRACT, ex);
            logger.debug("RejectIndEvent event received in state {}",
                         dialogue.getStateName());
        }
        String errorMessage;
        try {
            final int rejectType = event.getRejectType();
            errorMessage = "Component rejected reason " + rejectType;
        } catch (ParameterNotSetException ex) {
            logger.error("Failed to extract reject type {}", ex);
            errorMessage = "Component rejected unknown reason";
        }
        logger.error(errorMessage);
        terminate();
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }

    public void activate() {
        logger.debug("Nothing to do");
    }
    
    public void terminate() {
        // Release the dialogue ID
        logger.debug("deactivating");
        context.getDialogueManager().deactivate(dialogue);
        context.getProvider().releaseDialogueId(dialogue.getDialogueId());
    }
}
