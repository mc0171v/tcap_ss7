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
//    private IDialogueContext context;
    
    private final String stateName;

    private IDialogue dialogue;

    /***
     * Constructor to create a Dialogue state
     * @param context
     *     the context of this state
     * @param dialogue
     *     the dialogue in this state
     */
    public AbstractDialogueState(final String stateName,
                                 final IDialogue dialogue) {
//        this.context = context;
        this.dialogue = dialogue;
        this.stateName = stateName;
    }
    
    public AbstractDialogueState(final String stateName) {
        this.stateName = stateName;
    }
    
    public String getStateName() {
        return stateName;
    }

    /***
     * Get the context of the dialogue in this state
     * @return
     *     {@link IDialogueContext}
     */
//    public IDialogueContext getContext() {
//        return dialogue.getContext
//    }

    /***
     * Set the context of the dialogue in this state
     * @param context
     *     {@link IDialogueContext}
     */
//    public void setContext(final IDialogueContext context) {
//        this.context = context;
//    }

    /***
     * Get the dialogue in this state
     * @return
     *     {@link IDialogue}
     */
    public IDialogue getDialogue() {
        return dialogue;
    }

    /***
     * Set the dialogue in this state
     * @param dialogue
     *     {@link IDialogue}
     */
    public void setDialogue(final IDialogue dialogue) {
        this.dialogue = dialogue;
    }

    /***
     * Handle a {@link ComponentIndEvent} in this state
     * @param event
     *    the {@link ComponentIndEvent} to be handled
     */
    public abstract void handleEvent(final ComponentIndEvent event);

    /***
     * Handle a {@link DialogueIndEvent} in this state
     * @param event
     *    the {@link DialogueIndEvent} to be handled
     */
    public abstract void handleEvent(final DialogueIndEvent event);

    /**
     * Handle a dialogue/component request blocking
     * @param vbEx
     *     the {@link WouldBlockException} thrown
     */
    protected void handleWouldBlock(final WouldBlockException vbEx) {
        logger.debug("Received would block exception {} when starting dialogue, starting again", vbEx.getMessage());
        final IDialogueContext context = dialogue.getContext();
        context.getDialogueManager()
               .deactivate(dialogue);
        // release dialogueId
        context.getProvider().releaseDialogueId(dialogue.getDialogueId());

        context.startDialogue(dialogue.getRequest(),
                              dialogue.getLatch(),
                              dialogue.getType());
        //TODO Possible infinite loop - Need some flow control
    }

    /**
     * Handle a TCAP out of service exception
     * @param hdEx
     */
    protected void handleOutOfServiceException(final OutOfServiceException hdEx) {
        final String errorMessage = "Out of service: " + hdEx.getMessage();
        handleTerminatingException(errorMessage);

    }
    
    /**
     * Handle a TCAP SS7 exception
     * @param hdEx
     */
    protected void handleSs7Exception(final SS7Exception hdEx) {
        final String errorMessage = "SS7 exception: " + hdEx.getMessage();
        handleTerminatingException(errorMessage);
    }
    
    /**
     * Handle a vendor exception
     * @param vEx
     */
    protected void handleVendorException(final VendorException vEx) {
        final String errorMessage = "SS7 Vendor exception: " + vEx.getMessage();
        handleTerminatingException(errorMessage);
    }
    
    private void handleTerminatingException(final String errorMessage) {
        terminate();
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

    /***
     * Process a {@link BeginIndEvent}
     * @param event
     *     the event to handle
     */
    public void processBeginIndEvent(final BeginIndEvent event) {

        logger.debug("BeginIndEvent received in state {}", getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /***
     * Process a {@link ContinueIndEvent}
     * @param event
     *     the event to handle
     */
    public void processContinueIndEvent(final ContinueIndEvent event) {
        logger.debug("Continue IndEvent received in state {}", getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Process a {@link EndIndEvent}
     * @param event
     *     the event to handle
     */
    public void processEndIndEvent(final EndIndEvent event) {
        logger.debug("EndIndEvent received in state {}", getStateName());
        throw new UnexpectedPrimitiveException(event.getPrimitiveType());
    }

    /**
     * Process a {@link ProviderAbortIndEvent}
     * @param event
     *     the event to handle
     */
    public void processProviderAbortIndEvent(final ProviderAbortIndEvent event) {
        String errorMessage = "Provider abort received";
        try {
            errorMessage = errorMessage + ", abort reason is " + event.getPAbort();
            logger.debug("ProviderAbortIndEvent received in state {} for dialogue {}",
                     getStateName(),
                     event.getDialogueId());
        } catch (ParameterNotSetException ex) {
            logger.error("Parameter not set exception {}", ex);
            logger.debug("ProviderAbortIndEvent received in state {}",
                         getStateName());
        }
        logger.error(errorMessage);
        terminate();
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }

    /**
     * Process a {@link NoticeIndEvent}
     * @param event
     *     the event to handle
     */
    public void processNoticeIndEvent(final NoticeIndEvent event) {

        logger.debug("processNoticeIndEvent received in state {}", getStateName());
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

    /***
     * Process a {@link InvokeIndEvent}
     * @param event
     *     the event to handle
     */
    protected void processInvokeIndEvent(final InvokeIndEvent event) {
        logger.debug("InvokeIndEvent event received in state {}", getStateName());

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    /***
     * 
     * @param event
     */
    protected void processResultIndEvent(final ResultIndEvent event) {
        logger.debug("ResultIndEvent event received in state {}", getStateName());

        final int primitive = event.getPrimitiveType();
        throw new UnexpectedPrimitiveException(primitive);
    }

    /***
     * 
     * @param event
     */
    protected void processErrorIndEvent(final ErrorIndEvent event) {
        try {
            logger.debug("ErrorIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                         getStateName(),
                         event.getDialogueId(),
                         event.getInvokeId());
        } catch (Exception ex) {
            logger.error(FAILEDTOEXTRACT, ex);
            logger.debug("ErrorIndEvent event received in state {}",
                         getStateName());
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

    /***
     * 
     * @param event
     */
    protected void processLocalCancelIndEvent(final LocalCancelIndEvent event) {
        try {
            logger.debug("LocalCancelIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                         getStateName(),
                         event.getDialogueId(),
                         event.getInvokeId());
        } catch (Exception ex) {
            logger.error(FAILEDTOEXTRACT, ex);
            logger.debug("LocalCancelIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                         getStateName());
        }
        final String errorMessage = "Local cancel returned";
        logger.error(errorMessage);
        terminate();
        dialogue.setError(new Ss7ServiceException(errorMessage));
    }

    /***
     * 
     * @param event
     */
    protected void processRejectIndEvent(final RejectIndEvent event) {
        try {
        logger.debug("RejectIndEvent event received in state {} for dialogueId: {}, invokeId: {}",
                     getStateName(),
                     event.getDialogueId(),
                     event.getInvokeId());
        } catch (Exception ex) {
            logger.error(FAILEDTOEXTRACT, ex);
            logger.debug("RejectIndEvent event received in state {}",
                         getStateName());
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
        final IDialogueContext context = dialogue.getContext();
        context.getDialogueManager().deactivate(dialogue);
        context.getProvider().releaseDialogueId(dialogue.getDialogueId());
    }
}
