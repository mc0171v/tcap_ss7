package com.vennetics.bell.sam.ss7.tcap.common.listener;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ericsson.einss7.japi.VendorException;
import com.ericsson.einss7.japi.VendorIndEvent;
import com.ericsson.einss7.jtcap.TcapEventListener;
import com.ericsson.jain.protocol.ss7.tcap.JainTcapConfig;
import com.vennetics.bell.sam.ss7.tcap.common.component.requests.IComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.Dialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.DialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogue;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueManager;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.requests.IDialogueRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.states.IInitialDialogueState;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.SS7ConfigException;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.Ss7ServiceException;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.TcapErrorException;
import com.vennetics.bell.sam.ss7.tcap.common.exceptions.TcapUserInitialisationException;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.IListenerState;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerBound;
import com.vennetics.bell.sam.ss7.tcap.common.listener.states.ListenerReadyForTraffic;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;

import jain.protocol.ss7.JainSS7Factory;
import jain.protocol.ss7.SS7Exception;
import jain.protocol.ss7.tcap.ComponentIndEvent;
import jain.protocol.ss7.tcap.DialogueIndEvent;
import jain.protocol.ss7.tcap.JainTcapProvider;
import jain.protocol.ss7.tcap.JainTcapStack;
import jain.protocol.ss7.tcap.ParameterNotSetException;
import jain.protocol.ss7.tcap.TcapErrorEvent;
import jain.protocol.ss7.tcap.TcapUserAddress;

@Component
public class SamTcapEventListener implements ISamTcapEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SamTcapEventListener.class);

    private ISs7ConfigurationProperties configProperties;
        
    private IInitialDialogueState initialDialogueState;

    private IDialogueRequestBuilder dialogueRequestBuilder;
    
    private IComponentRequestBuilder componentRequestBuilder;
    
    private IListenerState state;
    private final TcapUserAddress origAddr;
    private TcapUserAddress destAddr;
    private IDialogueManager dialogueMgr;

    private JainTcapStack stack;
    private JainTcapProvider provider;

    private static final String SAM_TCAP = "SAM Project: TCAP statck";
    private final int std;

    private Vector<TcapUserAddress> addressVector;
    
    @PostConstruct
    public void init() {
        setConfiguration();
        initialise(true);
    }
    
    @PreDestroy
    public void destroy() {
        cleanup();
    }

    @Autowired
    SamTcapEventListener(final ISs7ConfigurationProperties configProperties,
                         @Qualifier("listenerUnbound") final IListenerState initialListenerState,
                         final IDialogueRequestBuilder dialogueRequestBuilder,
                         final IComponentRequestBuilder componentRequestBuilder,
                         final IInitialDialogueState initialDialogueState) {
        this.configProperties = configProperties;
        logger.debug("Configuration Properties:" + configProperties.toString());
        if (configProperties.getOrigAddress() != null) {
            this.origAddr = new TcapUserAddress(configProperties.getOrigAddress().getSpc(),
                                                configProperties.getOrigAddress().getSsn());
            logger.debug("Orig Address Set");
        } else {
            logger.error("Orig address not set");
            throw new SS7ConfigException("Orig address not set");
        }
        if (configProperties.getOrigAddress() != null) {
            this.destAddr = new TcapUserAddress(configProperties.getDestAddress().getSpc(),
                                                configProperties.getDestAddress().getSsn());
            logger.debug("Dest Address Set");
        } else {
            logger.error("Dest address not set");  
            throw new SS7ConfigException("Dest address not set");
        }
        this.dialogueMgr = new DialogueManager();
        this.state = initialListenerState;
        logger.debug("Initial Listener State Set");
        std = configProperties.getStd();
        this.componentRequestBuilder = componentRequestBuilder;
        logger.debug("Component Request Builder Set");
        this.dialogueRequestBuilder = dialogueRequestBuilder;
        logger.debug("Dialogue Request Builder Set");
        this.initialDialogueState = initialDialogueState;
        logger.debug("Initial Dialogue State Set");
        initialListenerState.setContext(this);
    }

    /**
     * Called both when init and also when re-init after processTcapError.
     * 
     * @param isFirst
     *            True if called at init, false if re-init.
     * @return True if init was successful.
     */
    public void initialise(final boolean isFirst) {
        logger.debug("initAppl called, isFirst: " + isFirst);
        try {
            stack = createJainTcapStack();
            logger.debug("Attaching provider to new JainTcapStack");
            provider = stack.createAttachedProvider();
            logger.debug("Attached provider to new JainTcapStack");
            logger.debug("Adding listener to Provider");
            provider.addTcapEventListener(this, origAddr);
            logger.debug("Added listener to Provider");

            // BindIndEvent will indicate that BE is ready
            logger.debug("Application initiated, wait for BindIndEvent");

        } catch (SS7Exception ex) {
            throw new TcapUserInitialisationException(ex.getMessage());
        } catch (VendorException ex) {
            throw new TcapUserInitialisationException(ex.getMessage());
        }
    }

    @SuppressWarnings(value = "deprecation")
    private JainTcapStack createJainTcapStack() throws jain.protocol.ss7.SS7PeerUnavailableException,
                                                jain.protocol.ss7.tcap.TcapException {
        JainSS7Factory.getInstance().setPathName("com.ericsson");
        logger.debug("Creating new JainTcapStack");
        final JainTcapStack newStack = (JainTcapStack) JainSS7Factory.getInstance()
                                                                     .createSS7Object("jain.protocol.ss7.tcap.JainTcapStackImpl");
        newStack.setProtocolVersion(std);

        newStack.setStackName(SAM_TCAP);
        logger.debug("Created new JainTcapStack");
        return newStack;
    }

    public void setConfiguration() {

        try {

            String configString = configProperties.getCpConfig();

            JainTcapConfig.getInstance().setConfigString(configString);

        } catch (VendorException vex) {
            throw new SS7ConfigException(vex.getMessage());
        }
    }

    @Override
    public void addUserAddress(final TcapUserAddress arg0) {
        // Not currently used.
        // Defined by JAIN TCAP API standard but
        // not used by the Ericsson implementation.
    }

    @Override
    public Vector<TcapUserAddress> getUserAddressList() {
        if (addressVector == null) {
            addressVector = new Vector<TcapUserAddress>();
            addressVector.add(origAddr);
        }
        return addressVector;
    }

    public synchronized void setDestinationAddress(final TcapUserAddress addr) {
        destAddr = addr;
    }

    public synchronized TcapUserAddress getDestinationAddress() {
        return destAddr;
    }

    @Override
    public void removeUserAddress(final TcapUserAddress arg0) {
        // Not currently used.
        // Defined by JAIN TCAP API standard but
        // not used by the Ericsson implementation.
    }

    public IDialogue startDialogue(final Object request,
                                   final CountDownLatch cDl) {
        final IDialogue dialogue = dialogueSetup(request, cDl);
        dialogue.activate();
        return dialogue;
    }
    
    public IDialogue joinDialogue(final int dialogueId) {
        final IDialogue dialogue = dialogueSetup(null, null);
        dialogue.setDialogueId(dialogueId);
        dialogue.activate();
        return dialogue;
    }
    
    private IDialogue dialogueSetup(final Object request, final CountDownLatch cDl) {
        final IDialogue dialogue = new Dialogue(this, provider, request);
        dialogue.setDialogueRequestBuilder(dialogueRequestBuilder);
        dialogue.setComponentRequestBuilder(componentRequestBuilder);
        dialogue.setLatch(cDl);
        IInitialDialogueState startState = null;
        try {
            startState = (IInitialDialogueState) initialDialogueState.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Ss7ServiceException("Failed to start dialogue");
        }
        startState.setContext(this);
        startState.setDialogue(dialogue);
        dialogue.setState(startState);
        return dialogue;
    }

    /**
     * General purpose cleanup method.
     * 
     * @param provider
     * @param stack
     * @exception jain.protocol.ss7.SS7Exception
     */
    public void cleanup() {
        try {
            logger.debug("Cleanup called");
            if (provider == null) {
                logger.debug("Cleanup: provider is null.");
                return;
            }
            try {
                provider.removeTcapEventListener(this);
                logger.debug("Removed JainTcapListener");
            } catch (jain.protocol.ss7.SS7ListenerNotRegisteredException ex) {
                logger.debug("Cleanup: Listener not registered");
            } catch (VendorException ex) {
                logger.debug("Cleanup: Error removing listener");
            }

            if (stack == null) {
                logger.debug("Cleanup: Stack is null.");
                return;
            }
            // delete JainTcapProvider, delete will do detach also
            stack.deleteProvider(provider);
            logger.debug("Deleted Provider");

        } catch (SS7Exception ex) {
            throw new TcapErrorException("Cleanup failed");
        }
    }

    public void clearAllDialogs() {
        // call clear on each Dialogue?
        dialogueMgr.clearAllDialogs();
    }

    @Override
    public void processComponentIndEvent(final ComponentIndEvent event) {
        state.handleEvent(event);
    }

    @Override
    public void processDialogueIndEvent(final DialogueIndEvent event) {
        state.handleEvent(event);
    }

    @Override
    public void processTcapError(final TcapErrorEvent event) {
        state.handleEvent(event);
    }

    @Override
    public void processVendorIndEvent(final VendorIndEvent event) {
        state.handleEvent(event);
    }

    public void setState(final IListenerState state) {
        this.state = state;
    }

    public boolean isBound() {
        if (this.state instanceof ListenerBound
         || this.state instanceof ListenerReadyForTraffic) {
            return true;
        }
        return false;
    }

    public boolean isReady() {
        if (this.state instanceof ListenerReadyForTraffic) {
            return true;
        }
        return false;
    }

    public IDialogueManager getDialogueManager() {
        return this.dialogueMgr;
    }

    public void setDialogueManager(final IDialogueManager manager) {
        this.dialogueMgr = manager;
    }
    
    public IDialogue getDialogue(final int dialogueId) {
        return dialogueMgr.lookUpDialogue(dialogueId);
    }

    public void deactivateDialogue(final IDialogue dialogue) {
        dialogueMgr.deactivate(dialogue);
    }

    public int getSsn() {
        try {
            return origAddr.getSubSystemNumber();
        } catch (ParameterNotSetException ex) {
            return 0;
        }
    }

    public TcapEventListener getTcapEventListener() {
        return this;
    }

    public TcapUserAddress getDestAddr() {
        return destAddr;
    }

    public TcapUserAddress getOrigAddr() {
        return origAddr;
    }

    public JainTcapProvider getProvider() {
        return provider;
    }
    
    public void setStack(final JainTcapStack stack) {
        this.stack = stack;
    }

    public void setProvider(final JainTcapProvider provider) {
        this.provider = provider;
    }

    public JainTcapStack getStack() {
        return stack;
    }
    
    public void setConfigProperties(final ISs7ConfigurationProperties configProperties) {
        this.configProperties = configProperties;
    }

    public ISs7ConfigurationProperties getConfigProperties() {
         return configProperties;
    }
    
    public void setInitialDialogueState(final IInitialDialogueState initialDialogueState) {
        this.initialDialogueState = initialDialogueState;
    }

    public void setDialogueRequestBuilder(final IDialogueRequestBuilder dialogueRequestBuilder) {
        this.dialogueRequestBuilder = dialogueRequestBuilder;
    }

    public void setComponentRequestBuilder(final IComponentRequestBuilder componentRequestBuilder) {
        this.componentRequestBuilder = componentRequestBuilder;
    }

}
