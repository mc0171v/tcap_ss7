package com.vennetics.bell.sam.ss7.tcap.common.listener;

import java.util.Map;
import java.util.Set;
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
import jain.protocol.ss7.tcap.UnableToDeleteProviderException;

@Component
public class SamTcapEventListener implements ISamTcapEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SamTcapEventListener.class);

    private ISs7ConfigurationProperties configProperties;
        
    private Set<IInitialDialogueState> initialDialogueStates;

    private Set<IDialogueRequestBuilder> dialogueRequestBuilders;
    
    private Set<IComponentRequestBuilder> componentRequestBuilders;
    
    private IListenerState state;
    private final TcapUserAddress origAddr;
    private TcapUserAddress destAddr;
    private IDialogueManager dialogueMgr;

    private JainTcapStack stack;
    private JainTcapProvider provider;

    private static final String SAM_TCAP = "SAM Project: TCAP stack";
    private static final String SLICE_IDENTIFIER = "EINSS7_DID_SLICING_ID";
    private final int std;

    private Vector<TcapUserAddress> addressVector;

    @Autowired
    SamTcapEventListener(final ISs7ConfigurationProperties configProperties,
                         @Qualifier("listenerUnbound") final IListenerState initialListenerState,
                         final Set<IInitialDialogueState> initialDialogueState,
                         final Set<IDialogueRequestBuilder> dialogueRequestBuilder,
                         final Set<IComponentRequestBuilder> componentRequestBuilder) {
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
        if (configProperties.getDestAddress() != null) {
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
        this.componentRequestBuilders = componentRequestBuilder;
        logger.debug("Component Request Builders Set");
        this.dialogueRequestBuilders = dialogueRequestBuilder;
        logger.debug("Dialogue Request Builders Set");
        this.initialDialogueStates = initialDialogueState;
        logger.debug("Initial Dialogue States Set");
        initialListenerState.setContext(this);
    }
    
    
    @PostConstruct
    public void init() {
        setConfiguration();
        initialise();
    }
    
    @PreDestroy
    public void destroy() {
        cleanup();
    }

    /**
     * Called both when init and also when re-init after processTcapError.
     * 
     * @return True if init was successful.
     */
    @Override
    public void initialise() {
        logger.debug("initialise called");
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
            logger.error("SS7Exception thrown {}", ex);
            throw new TcapUserInitialisationException(ex.getMessage());
        } catch (VendorException ex) {
            logger.error("VendorException thrown {}", ex);
            throw new TcapUserInitialisationException(ex.getMessage());
        }
    }

    @SuppressWarnings(value = "deprecation")
    public JainTcapStack createJainTcapStack() throws jain.protocol.ss7.SS7PeerUnavailableException,
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

    @Override
    public void setConfiguration() {
        try {
            String configString = configProperties.getCpConfig();
            final Map<String, String> env = System.getenv();
            if (env.containsKey(SLICE_IDENTIFIER)) {
                final String sliceIdStr = env.get(SLICE_IDENTIFIER);
                configString = configString + " -D" + SLICE_IDENTIFIER + "=" + sliceIdStr;
            }
            logger.debug("Setting config: {}", configString);
            JainTcapConfig.getInstance().setConfigString(configString);

        } catch (VendorException vex) {
            logger.error("Could not set CP config {}", vex);
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

    @Override
    public synchronized TcapUserAddress getDestinationAddress() {
        return destAddr;
    }

    @Override
    public void removeUserAddress(final TcapUserAddress arg0) {
        // Not currently used.
        // Defined by JAIN TCAP API standard but
        // not used by the Ericsson implementation.
    }

    @Override
    public IDialogue startDialogue(final Object request,
                                   final CountDownLatch cDl,
                                   String type) {
        final IDialogue dialogue = dialogueSetup(request, cDl, type);
        dialogue.activate();
        return dialogue;
    }
   
    @Override
    public IDialogue joinDialogue(final int dialogueId, String type) {
        final IDialogue dialogue = dialogueSetup(null, null, type);
        dialogue.setDialogueId(dialogueId);
        dialogue.activate();
        return dialogue;
    }
    
    private IDialogue dialogueSetup(final Object request, final CountDownLatch cDl, String type) {
        final IDialogue dialogue = new Dialogue(this, request);
        dialogue.setLatch(cDl);
        IInitialDialogueState startState = getInitialDialogueState(type).newInstance();
        startState.setDialogue(dialogue);
        dialogue.setState(startState);
        dialogue.setType(type);
        return dialogue;
    }
    
    public IInitialDialogueState getInitialDialogueState(final String type) {
        for (final IInitialDialogueState state : initialDialogueStates) {
            logger.debug("check equals state = {}", state);
            if (state.getStateType().equals(type)) {
                logger.debug("equals state = {}", state);
                return state;
            }
        }
        logger.error("No maching initial dialogue state");
        return null;
    }

    /**
     * General purpose cleanup method.
     * 
     * @param provider
     * @param stack
     * @exception jain.protocol.ss7.SS7Exception
     */
    @Override
    public void cleanup() {
        try {
            logger.debug("Cleanup called");
            if (provider == null) {
                logger.debug("Cleanup: provider is null.");
                return;
            }
            removeListener();
            if (stack == null) {
                logger.debug("Cleanup: Stack is null.");
                return;
            }
            // delete JainTcapProvider, delete will do detach also
            stack.deleteProvider(provider);
            logger.debug("Deleted Provider");

        } catch (UnableToDeleteProviderException ex) {
            logger.error("Cleanup failed {}", ex);
            throw new TcapErrorException("Cleanup failed");
        }
    }
    
    private void removeListener() {
        try {
            provider.removeTcapEventListener(this);
            logger.debug("Removed JainTcapListener");
        } catch (jain.protocol.ss7.SS7ListenerNotRegisteredException ex) {
            logger.debug("Cleanup: Listener not registered: {} ", ex);
        } catch (VendorException ex) {
            logger.error("Cleanup: Error removing listener: {}", ex);
        } catch (SS7Exception ex) {
            logger.error("Cleanup SS7 Error: {}", ex);
            throw new TcapErrorException("Cleanup SS7 Error: {}" + ex.getMessage());
        }
    }

    @Override
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

    @Override
    public void setState(final IListenerState state) {
        this.state = state;
    }

    @Override
    public boolean isBound() {
        if (this.state instanceof ListenerBound
         || this.state instanceof ListenerReadyForTraffic) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isReady() {
        if (this.state instanceof ListenerReadyForTraffic) {
            return true;
        }
        return false;
    }

    @Override
    public IDialogueManager getDialogueManager() {
        return this.dialogueMgr;
    }

    @Override
    public void setDialogueManager(final IDialogueManager manager) {
        this.dialogueMgr = manager;
    }
    
    @Override
    public IDialogue getDialogue(final int dialogueId) {
        return dialogueMgr.lookUpDialogue(dialogueId);
    }

    @Override
    public void deactivateDialogue(final IDialogue dialogue) {
        dialogueMgr.deactivate(dialogue);
    }

    @Override
    public int getSsn() {
        try {
            return origAddr.getSubSystemNumber();
        } catch (ParameterNotSetException ex) {
            logger.error("SSN not set: {}", ex);
            return 0;
        }
    }

    @Override
    public TcapEventListener getTcapEventListener() {
        return this;
    }

    @Override
    public TcapUserAddress getDestAddr() {
        return destAddr;
    }

    @Override
    public TcapUserAddress getOrigAddr() {
        return origAddr;
    }

    @Override
    public JainTcapProvider getProvider() {
        return provider;
    }
    
    @Override
    public void setStack(final JainTcapStack stack) {
        this.stack = stack;
    }

    @Override
    public void setProvider(final JainTcapProvider provider) {
        this.provider = provider;
    }

    @Override
    public JainTcapStack getStack() {
        return stack;
    }
    
    public void setConfigProperties(final ISs7ConfigurationProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public ISs7ConfigurationProperties getConfigProperties() {
         return configProperties;
    }
    
    public void setInitialDialogueState(final Set<IInitialDialogueState> initialDialogueStates) {
        this.initialDialogueStates = initialDialogueStates;
    }

    public void setDialogueRequestBuilder(final Set<IDialogueRequestBuilder> dialogueRequestBuilders) {
        this.dialogueRequestBuilders = dialogueRequestBuilders;
    }

    public void setComponentRequestBuilder(final Set<IComponentRequestBuilder> componentRequestBuilders) {
        this.componentRequestBuilders = componentRequestBuilders;
    }
    
    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext#getDialogueRequestBuilder()
     */
    @Override
    public IDialogueRequestBuilder getDialogueRequestBuilder(String type) {
        for (final IDialogueRequestBuilder builder : dialogueRequestBuilders) {
            if (builder.getBuilderType().equals(type)) {
                return builder;
            }
        }
        logger.error("No maching dialogue request builder");
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.vennetics.bell.sam.ss7.tcap.common.dialogue.IDialogueContext#getComponentRequestBuilder()
     */
    @Override
    public IComponentRequestBuilder getComponentRequestBuilder(String type) {
        for (final IComponentRequestBuilder builder : componentRequestBuilders) {
            if (builder.getBuilderType().equals(type)) {
                return builder;
            }
        }
        logger.error("No maching component request builder");
        return null;
    }

}
