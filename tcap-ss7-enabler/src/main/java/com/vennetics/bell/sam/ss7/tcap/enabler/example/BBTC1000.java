/*===========================================================================*/
/*
 *
 * Copyright (C) Ericsson AB 2005 - All rights reserved.
 *
 * No part of this program may be reproduced in any form without the written
 * permission of the copyright owner.
 *
 * The contents of this program are subject to revision without notice due to
 * continued progress in methodology, design and manufacturing.
 * Ericsson shall have no liability for any error or damage of any kind
 * resulting from the use of this program.
 *
 *
 */
package com.vennetics.bell.sam.ss7.tcap.enabler.example;

//java imports
import java.io.*;
import java.util.*;

//ericsson imports
import com.ericsson.einss7.japi.*;
import com.ericsson.einss7.jtcap.*;
import com.ericsson.jain.protocol.ss7.tcap.*;

//jain imports
import jain.protocol.ss7.*;
import jain.protocol.ss7.tcap.*;
import jain.protocol.ss7.tcap.component.*;
import jain.protocol.ss7.tcap.dialogue.*;



/**
 * TestName: HA Example: file config and one active BE.
 * <P>
 * Purpose: Used in JTCAP javadoc as an example, but is also part of Basic Test.
 * Other test cases extends this one. ListenerA1000 and ListenerB1000
 * are different ss7-nodes (A and B side). If the test stub is used
 * then it will replace the ListenerB1000. <BR>
 * The TCAP primitive sequence:
 *<PRE>
 *      A-side:
 *
 *      ListenerA1000            TCAP
 *
 *           |    INVOKE_req      | OPERATION_CONTINUE_DIALOGUE
 *           |------------------->|
 *           |     BEGIN_req      |
 *           |------------------->|
 *           |  CONTINUE_ind      |
 *           |<-------------------|
 *           |    RESULT_ind      |
 *           |<-------------------|
 *           |    INVOKE_req      | OPERATION_END_DIALOGUE
 *           |------------------->|
 *           |  CONTINUE_req      |
 *           |------------------->|
 *           |       END_ind      |
 *           |<-------------------|
 *           |    RESULT_ind      |
 *           |<-------------------|
 *
 *            (new dialogue ...)
 *
 *      B-side:
 *
 *          TCAP            ListenerB1000 (or test stub)
 *           |                    |
 *           |     BEGIN_ind      |
 *           |------------------->|
 *           |    INVOKE_ind      | OPERATION_CONTINUE_DIALOGUE
 *           |------------------->|
 *           |    RESULT_req      |
 *           |<-------------------|
 *           |  CONTINUE_req      |
 *           |<-------------------|
 *           |  CONTINUE_ind      |
 *           |------------------->|
 *           |    INVOKE_ind      | OPERATION_END_DIALOGUE
 *           |------------------->|
 *           |    RESULT_req      |
 *           |<-------------------|
 *           |       END_req      |
 *           |<-------------------|
 *
 *            (new dialogue ...)
 *
 *
 *</PRE>
 *
 */
public class BBTC1000 extends TcapApiTest {


    public static final byte[] OPERATION_CONTINUE_DIALOGUE =  { 0x01};
    public static final byte[] OPERATION_END_DIALOGUE =       { 0x02};
    public static final byte[] OPERATION_END_LAST_DIALOGUE  = { 0x03};
    public static final byte[] ATI  = { 0x47};

    protected int m_bbTestTimeout =
        Integer.parseInt(System.getProperty("BB_TEST_TIMEOUT",
                                            "30000"));
    protected int m_bbTestDialogues =
        Integer.parseInt(System.getProperty("BB_TEST_DIALOGS",
                                            "1"));
    protected final static long BB_TEST_INVOKE_TIMEOUT =
        Long.parseLong(System.getProperty("BB_TEST_INVOKE_TIMEOUT",
                                          "30000"));



    /**
     * Constructor
     *
     * @param std    Which TCAP standard, for example
     *               DialogueConstants.PROTOCOL_VERSION_ITU_97
     */
    public BBTC1000(int std) {
        super(std);
        m_testName = getClass().getName();

        setConfiguration();
    }

    /**
     * Override this with number of expected TcBindIndEvent.
     * @return The number expeced.
     */
    int getExpectedTcBindIndEvents() {
        return 1;
    }

    /**
     * Return a description of the test case.
     *
     * @return See above.
     */
    public String getTestName() {
        return "[HA Example: file config and one active BE      ]";
    }


    /**
     * Printed at startup of test.
     */
    public void printTestHeader() {
        System.out.print(m_testName + ": " + getTestName() +
                         " Started (timeout=" +
                         m_bbTestTimeout + "ms)... ");
    }


    /**
     *Entry point for A side.
     */
    public synchronized void executeA() {
        Listener1000 list = createListenerA1000(m_userAddressA1,
                                                m_userAddressB1);

        System.out.println("Listener created: " + list.toString());
        System.out.println("List with Adresses:");
        System.out.println("" + ((ListenerA1000)list).toStringWithAdresses());

        printTestHeader();

        list.executeAppl();
        printTestResult(list);
    }

    /**
     *Entry point for B side.
     */
    public synchronized void executeB() {
        Listener1000 list = createListenerB1000(m_userAddressB1,
                                                m_userAddressA1);
        System.out.println("Listener created: " + list.toString());
        System.out.println("List with Adresses:");
        System.out.println("" + ((ListenerB1000)list).toStringWithAdresses());
        list.executeAppl();
        printTestResult(list);
    }

    /**
      * Set configuration properties.
      */
    protected void setConfiguration() {

        try {
            //NOTE: a real application would NOT hard code the
            //configString as in this example, get the configString from
            //some data base or file instead:
            String configString =
                //"-DEIN_JCP_CONFIG_NAME=/Users/martincaldwell/Documents/workspace/ss7/src/jtcapblackbox/cp.cnf " +
                //"-DEINSS7_JCP_CONFIG_FILE_FORMAT=ascii " +
                "-DEIN_JCP_USER_ID=USER01_ID " +
                "-DEINSS7_ASYNCH_BEHAVIOUR_ON=true " +
                "-DEINSS7_DID_SLICING_ID=1 " +
                "-DEINSS7_DID_NUM_OF_SLICES=1 " +
                "-DEINSS7_JDID_SUPPORT_ON=true" +
                "-DEIN_HD_USER_INSTANCE=0" +
                "-DEIN_JCP_CPMANAGER_ADDRESS=10.87.79.209:6669" +
                "-DEIN_HD_ATTACH_INSTANCES=1";

            JainTcapConfig.getInstance().setConfigString(configString);

        } catch (VendorException vex) {
            throw new RuntimeException("Error in config\n" +
            		ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(vex));
        }
    }

    /**
     *Listener factory method for A side.
     */
    protected Listener1000 createListenerA1000(TcapUserAddress origAddr,
                                               TcapUserAddress destAddr) {
        return new ListenerA1000(m_userAddressA1,
                                 m_userAddressB1);
    }


    /**
     *Listener factory method for B side.
     */
    protected Listener1000 createListenerB1000(TcapUserAddress origAddr,
                                               TcapUserAddress destAddr) {
        return new ListenerB1000(m_userAddressB1,
                                 m_userAddressA1);
    }



    /**
     * Check if test was OK, print result.
     * @param listener
     */
    protected void printTestResult(Listener1000 listener) {
        if (listener.isTestSuccess()) {
            if (BB_TEST_TRACE) {
                System.out.println("\n\n");
            }
            System.out.println(getClass().getName() +
                               "\tPASSED");
        } else {
            String error = listener.getError();
            String error2 = getClass().getName() +
                " NOT PASSED!!!." + error;
            System.out.println("\n\n****" + error2 +
                               "\n\n");

            TcapApiTest.testSuiteError(error2);

        }
        doAtEndOfTest();
    }



    /**
     */
    protected void doAtEndOfTest() {

    }


    /**
     * Trace.
     * @param src The calling object.
     * @param s The trace string.
     */
    protected void DEBUG_TRACE(Object src, String s) {
        if (BB_TEST_TRACE) {
            System.out.println(m_testName +
                               "::" + src + ": " + s);
            //Tools.println(src, m_testName + "::" + s);
        }
    }


    /**
     * Error reporting.
     */
    interface TestErrorHandler {
        void error(String errorStr);
    }


    /**
     *Purpose: Used by both A and B side, override operations if needed.
     */
    protected abstract class Listener1000 implements TcapEventListener,
        TestErrorHandler {

        protected JainTcapStack m_stack;
        protected JainTcapProvider m_provider;
        protected int m_dialogsReady;
        protected boolean m_testFailed = false;
        private String m_error = "Errors:";
        protected boolean m_testCleaned = false;

        protected Vector m_addressVector;
        protected TcapUserAddress m_origAddr;
        protected TcapUserAddress m_destAddr;
        protected DialogueManager m_dialogueMgr;
        protected boolean m_isStarted = false;
        protected boolean m_isOutOfService = false;
        protected int m_numberOfInvokesMade = 0;


        //running two invokes for each dialogue as default
        //override Listener1000 class and use (in the constructor)
        // setMaxNumberOfInvokes(NUMBER_OF_INVOKES);
        // to change default value
        protected int m_maxNumberOfInvokes = 2;
        protected int m_tcBindIndEventsReceived = 0;

        /**
         * Default c-tor.
         */
        //public Listener1000() {
        //}


        /**
         * C-tor.
         *
         * @param origAddr
         * @param destAddr
         */
        public Listener1000(TcapUserAddress origAddr,
                            TcapUserAddress destAddr)
        {
            m_origAddr = origAddr;
            m_destAddr = destAddr;
            m_dialogueMgr = new DialogueManager(this);
        }



        /**
         * Check different test output variables for success.
         *
         * @return True if success.
         */
        boolean isTestSuccess() {
            if (m_tcBindIndEventsReceived < getExpectedTcBindIndEvents()) {
                error("Wrong number of TcBindIndEvents received, got: " +
                      m_tcBindIndEventsReceived + ", expected: " +
                      getExpectedTcBindIndEvents());
            }
            if (m_dialogsReady != m_bbTestDialogues) {
                DEBUG_TRACE(this, "m_dialogsReady: " + m_dialogsReady);
                error("Wrong number of dialogs ready, ready: " +
                      m_dialogsReady +
                      ", expected: " + m_bbTestDialogues);
            }
            if (m_dialogueMgr.isDialogueLeft()) {
                DEBUG_TRACE(this, "m_dialogueMgr.isDialogueLeft(): " +
                            m_dialogueMgr.isDialogueLeft());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                PrintWriter pw = new PrintWriter(os,true);
                pw.println("Dialogues still active!");
                m_dialogueMgr.printDialoguesLeft(pw);
                error(os.toString());
            }

            return !m_testFailed;
        }


        /**
         * All the errors as one string.
         *
         * @return See above.
         */
        String getError() {
            return m_error;
        }


        /**
         *Handle error events. The Ericsson JTCAP has non-JAIN sub classes
         *of TcapErrorEvent that are optional to use by the TC-user.
         */
        public synchronized void processTcapError(TcapErrorEvent tcapError) {
            DEBUG_TRACE(this, "processTcapError called: " + tcapError + "\n" +
            		ericsson.ein.ss7.commonparts.util.Tools.getStackTrace((Exception)tcapError.getError()));

            handleProcessTcapError(tcapError);
        }


        /**
         * Called from processTcapError, if recover is needed.
         * @param tcapError
         */
        public void handleProcessTcapError(TcapErrorEvent tcapError) {
            //            if (m_testCleaned) {
            //                handleGhostProvider((JainTcapProvider)tcapError.getSource());
            //                return;
            //            } else {
            //                //cleanup before calling initAppl again
            //                cleanup(m_provider, m_stack);
            //
            //                //must clear old dialogues
            //                m_dialogueMgr.clearAllDialogs();
            //           }


            if (m_testCleaned) {
                TcapApiTest.testSuiteWarning
                    (m_testName +
                     ": A ghost-processTcapError was waiting to " +
                     "begin recover, it was aborted.");
                return;
            }



            //cleanup before calling initAppl again
            cleanup(m_provider, m_stack);

            //must clear old dialogues in case there are started dialogues
            //in the old provider
            m_dialogueMgr.clearAllDialogs();



            //try to get things going again
            DEBUG_TRACE(this, "Recovery, calling initAppl.");
            initAppl(false);
            DEBUG_TRACE(this, "initAppl returned, result is asynchronous");
        }


        /**
         * Component Events dispatching.
         *
         * @param event  A specific component.
         */
        public void processComponentIndEvent(ComponentIndEvent event) {
            //DEBUG_TRACE(this, "Component event received.");

            if (m_testCleaned) {
                //handleGhostProvider((JainTcapProvider)event.getSource());
                return;
            }

            try {
                int primitive = event.getPrimitiveType();
                switch (primitive) {
                case TcapConstants.PRIMITIVE_INVOKE:
                    processInvokeIndEvent((InvokeIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_RESULT:
                    processResultIndEvent((ResultIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_ERROR:
                    processErrorIndEvent((ErrorIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_LOCAL_CANCEL:
                    processLocalCancelIndEvent((LocalCancelIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_REJECT:
                    processRejectIndEvent((RejectIndEvent)event);
                    break;
                default:
                    throw new RuntimeException("Unexpected primitive:\n" +
                                               event);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }

        /**
         * Component event.
         */
        public void processInvokeIndEvent(InvokeIndEvent event)
        throws SS7Exception, VendorException {

            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }

        /**
         * Receive a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is received.
         */
        public void processVendorIndEvent(VendorIndEvent event) {
            if (m_testCleaned) {
                //handleGhostProvider((JainTcapProvider)event.getSource());
                return;
            }
            //try {
            int eventType = event.getVendorEventType();
            switch (eventType) {
            case VendorComponentIndEvent.VENDOR_EVENT_COMPONENT_IND:
                //fall through...
            case VendorDialogueIndEvent.VENDOR_EVENT_DIALOGUE_IND:
                processVendorDialogueIndEvent((VendorDialogueIndEvent)
                                              event);
                break;
            case VendorIndEvent.VENDOR_EVENT_GENERAL_IND:
                processVendorGeneralIndEvent(event);
                break;
            default:
                error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                      ( new RuntimeException("Unexpected primitive:\n" +
                                             event)));
            }
            //} catch (Exception ex) {
            //    error(Tools.getStackTrace(ex));
            //}
        }//processVendorIndEvent



        /**
         * Process a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public void processVendorGeneralIndEvent(VendorIndEvent event) {
            DEBUG_TRACE(this, "processVendorGeneralIndEvent");

            int primitive = event.getPrimitiveType();
            switch (primitive) {
            case TcBindIndEvent.PRIMITIVE_TC_BIND_IND:
                m_tcBindIndEventsReceived++;
                processTcBindIndEvent((TcBindIndEvent)event);
                break;
            case TcStateIndEvent.PRIMITIVE_TC_STATE_IND:
                processTcStateIndEvent((TcStateIndEvent)event);
                break;
            case TcDialoguesLostIndEvent.PRIMITIVE_TC_DIALOGUES_LOST_IND:
                processTcDialoguesLostIndEvent((TcDialoguesLostIndEvent)
                                               event);
                break;
            default:
                error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                      ( new RuntimeException("Unexpected primitive:\n" +
                                             event)));
            }
        }//processVendorGeneralIndEvent


        /**
         * Process a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public
            void processVendorDialogueIndEvent(VendorDialogueIndEvent event) {
            DEBUG_TRACE(this, "processVendorDialogueIndEvent");

            int dialogueId = event.getDialogueId();
            Dialogue dialogue = m_dialogueMgr.lookUpDialogue(dialogueId);

            dialogue.handleVendorDialogueIndEvent(event);

        }//processVendorDialogueIndEvent


        /**
         * Process a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public void processTcBindIndEvent(TcBindIndEvent event) {
            DEBUG_TRACE(this, "processTcBindIndEvent " + event.getSubSystemId());
        }


        /**
         * Process a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public void processTcStateIndEvent(TcStateIndEvent event) {

            DEBUG_TRACE(this, "processTcStateIndEvent");

        }// processTcStateIndEvent


        /**
         * Check if the event indicates that the addr is ready for traffic.
         *
         * @param event
         * @param addr
         * @return True if ready.
         */
        protected boolean isReadyForTraffic(TcStateIndEvent event,
                                            TcapUserAddress addr) {
            //check that user is available
            //if (event.getUserStatus() !=
            //    TcStateIndEvent.USER_UNAVAILABLE) {
            if (event.getUserStatus() ==
                TcStateIndEvent.USER_UNAVAILABLE) {
                DEBUG_TRACE(this, "TcStateIndEvent.USER_UNAVAILABLE");

                return false;
            }

            //extract SPC and SSN from addr
            byte[] addrSpc = null;
            int addrSsn = -1;
            try {
                addrSpc = addr.getSignalingPointCode();
                addrSsn = addr.getSubSystemNumber();
            } catch (Exception ex) {
                error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(ex));
                return false;
            }

            //check that SPC in addr is the same as affected SPC
            byte[] affectedSpc = event.getAffectedSpc();

            if (affectedSpc.length != addrSpc.length) {
                return false;
            }

            for (int i=0; i<affectedSpc.length; i++) {
                if (affectedSpc[i] != addrSpc[i]) {
                    return false;
                }
            }

            //if SSN also matches then ready for traffic
            return event.getAffectedSsn() == addrSsn;
        }//isReadyForTraffic


        /**
         * Process a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public
            void processTcDialoguesLostIndEvent(TcDialoguesLostIndEvent event) {

            DEBUG_TRACE(this, "processTcDialoguesLostIndEvent");
            //handle lost dialogues here...

            m_dialogueMgr.cleanUpLostDialogues(event);
            beginDialogue();
        }


        /**
         *  Component event.
         */
        public void processResultIndEvent(ResultIndEvent event)
        throws SS7Exception {

            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         *  Component event.
         */
        public void processErrorIndEvent(ErrorIndEvent event)
        throws SS7Exception {

            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         *  Component event.
         */
        public void processLocalCancelIndEvent(LocalCancelIndEvent event)
        throws SS7Exception {

            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }

        /**
         *  Component event.
         */
        public void processRejectIndEvent(RejectIndEvent event)
        throws SS7Exception
        {
            final int rejectType = event.getRejectType();
            switch (rejectType) {
            case ComponentConstants.REJECT_TYPE_USER:
                processRejectUserIndEvent(event);
                break;
            case ComponentConstants.REJECT_TYPE_REMOTE:
                processRejectUserIndEvent(event);
                break;
            case ComponentConstants.REJECT_TYPE_LOCAL:
                processRejectUserIndEvent(event);
                break;
            default:
                error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                      ( new RuntimeException("Unknown reject type: " +
                                             rejectType)));
            }
        }


        /**
         *  Component event.
         */
        public void processRejectUserIndEvent(RejectIndEvent event)
        throws SS7Exception {
            DEBUG_TRACE(this, "RejectIndEvent REJECT_TYPE_USER received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         *  Component event.
         */
        public void processRejectRemoteIndEvent(RejectIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this,
                        "RejectIndEvent REJECT_TYPE_REMOTE received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         *  Component event.
         */
        public void processRejectLocalIndEvent(RejectIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this,
                        "RejectIndEvent REJECT_TYPE_LOCAL received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }



        /**
         * Dialogue Events dispatching.
         *
         * @param event
         */
        public void processDialogueIndEvent(DialogueIndEvent event) {
            //DEBUG_TRACE(this, "Dialogue event received.");

            if (m_testCleaned) {
                //handleGhostProvider((JainTcapProvider)event.getSource());
                return;
            }

            try {
            Dialogue dialogue =
                m_dialogueMgr.lookUpDialogue(event.getDialogueId());
            if(dialogue == null) {
            	System.out.println("Dialogue==null");
            	return;
            }
            } catch(MandatoryParameterNotSetException e) {
               System.out.println("Dialog does not exists");
               e.printStackTrace();
               return;
            }

            try {
                int primitive = event.getPrimitiveType();
                switch (primitive) {
                case TcapConstants.PRIMITIVE_BEGIN:
                    processBeginIndEvent((BeginIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_CONTINUE:
                    processContinueIndEvent((ContinueIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_END:
                    processEndIndEvent((EndIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_PROVIDER_ABORT:
                    processProviderAbortIndEvent((ProviderAbortIndEvent)event);
                    break;
                case TcapConstants.PRIMITIVE_NOTICE:
                    processNoticeIndEvent((NoticeIndEvent)event);
                    break;
                default:
                    error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                          ( new RuntimeException("Unexpected primitive:\n" +
                                                 event)));
                }
            } catch (SS7Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }


        /**
         * Dialogue event.
         */
        public void processBeginIndEvent(BeginIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "BeginIndEvent received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         * Dialogue event.
         */
        public void processContinueIndEvent(ContinueIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "Continue IndEvent received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         * Dialogue event.
         */
        public void processEndIndEvent(EndIndEvent event) throws SS7Exception {
            DEBUG_TRACE(this, "EndIndEvent received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }


        /**
         * Dialogue event.
         */
        public void processProviderAbortIndEvent(ProviderAbortIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "ProviderAbortIndEvent received.");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }

        /**
         * Dialogue event.
         */
        public void processNoticeIndEvent(NoticeIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "processNoticeIndEvent");
            error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                  ( new RuntimeException("Unexpected primitive:\n" + event)));
        }

        /**
         * JAIN callback, ignored.
         */
        public void addUserAddress(TcapUserAddress userAddress) {
            DEBUG_TRACE(this, "addUserAddress called");
        }


        /**
         * JAIN callback, ignored.
         */
        public void removeUserAddress(TcapUserAddress userAddress) {
            DEBUG_TRACE(this, "removeUserAddress called");
        }


        /**
         * JAIN callback.
         */
        public Vector getUserAddressList() {
            //DEBUG_TRACE(this, "getUserAddressList called");
            if (m_addressVector == null) {
                m_addressVector = new Vector();
                m_addressVector.add(m_origAddr);
            }
            return m_addressVector;
        }


        /**
         * The entry point for this listener.
         */
        public synchronized void executeAppl() {
            DEBUG_TRACE(this, "executeAppl called");
            initAppl(true);
            //application will start after BindIndEvent is received
            //wait until test is ready
            try {
                this.wait(m_bbTestTimeout);
                DEBUG_TRACE(this, "wait(BB_TEST_TIMEOUT) returned");
            } catch (Exception ex) {
                error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(ex));
                return;
            }

            this.cleanupTestCase();
        }


        /**
         *
         * @param addr
         */
        public synchronized void setDestinationAddress(TcapUserAddress addr) {
            m_destAddr = addr;
        }


        /**
         */
        protected void startAppl() {
            beginDialogue(); //begin the first dialogue
        }


        /**
         * Called both when init and also when re-init after processTcapError.
         * @param isFirst True if called at init, false if re-init.
         * @return True if init was successful.
         */
        protected void initAppl(boolean isFirst) {
            DEBUG_TRACE(this, "initAppl called, isFirst: " + isFirst);
            try {
                m_stack = createJainTcapStack(m_std);
                m_provider = m_stack.createAttachedProvider();
                m_provider.addTcapEventListener(this, m_origAddr);

                //BindIndEvent will indicate that BE is ready
                DEBUG_TRACE(this,
                            "Application initiated, wait for BindIndEvent");

            } catch (SS7Exception ex) {
                initError(ex, isFirst);
            } catch (VendorException ex) {
                initError(ex, isFirst);
            }
        }



        /**
         * Check if init error.
         *
         * @param ex
         * @param isFirst
         */
        protected void initError(Exception ex, boolean isFirst) {
            if (isFirst) {
                error(
                     "Init error (check configuration and local addresses)." +
                     "\n" + ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(ex));
            }
            cleanup(m_provider, m_stack);
        }


        /**
         *Begin a new TCAP dialogue. Called by main thread the first time,
         *then called by the thread executing processResultIndEvent.
         */
        protected void beginDialogue(OutBeginDialogue outParams) {
            DEBUG_TRACE(this, "beginDialogue called");
            // set number of invokes to zero for next dialogue
            m_numberOfInvokesMade = 0;
            EventObject currentReq = null;
            InvokeReqEvent invokeReq = null;
            BeginReqEvent beginReq = null;
            int dialogueId = -1;
            try {
                int ssn = m_origAddr.getSubSystemNumber();
                dialogueId=m_provider.getNewDialogueId(ssn);
                int invokeId=m_provider.getNewInvokeId(dialogueId);
                currentReq = invokeReq =
                    createInvokeReq(this,
                                    invokeId,
                                    getInvokeOperationCode(),
                                    true); //withParams

                invokeReq.setDialogueId(dialogueId);
                invokeReq.setTimeOut(BB_TEST_INVOKE_TIMEOUT);
                //set class 1 operation (report failure or success)
                invokeReq.setClassType(ComponentConstants.CLASS_1);
                DEBUG_TRACE(this, "Sending invoke with invokeID: "
                            + invokeId + " ...");
                m_dialogueMgr.activate(new Dialogue(this,
                                                    dialogueId,
                                                    ssn,
                                                    m_provider,
                                                    this));
                m_provider.sendComponentReqEventNB(invokeReq);

                //----- Build begin request:
                currentReq = beginReq = createBeginReq(this,
                                                       dialogueId,
                                                       m_origAddr,
                                                       m_destAddr);
                DEBUG_TRACE(this, "Sending begin...");
                m_provider.sendDialogueReqEventNB(beginReq);

                if (outParams != null) {
                    outParams.dialogueId = dialogueId;
                    outParams.invokeId = invokeId;
                }
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
         *Begin a new TCAP dialogue. Called by main thread the first time,
         *then called by the thread executing processResultIndEvent.
         */
        protected void beginDialogue() {
            beginDialogue(null);
        }


        /**
         * Return the operation code to send to B-side in an INVOKE.
         * @return The operation code.
         */
        protected byte[] getInvokeOperationCode() {
            throw new RuntimeException("B-side does not send any invoke.");
        }


        /**
         * Called when cleaning up after performed test case.
         * @exception jain.protocol.ss7.SS7Exception
         */
        protected synchronized void cleanupTestCase() {
            //NOTE: processTcapError and this method must be synchronized
            DEBUG_TRACE(this, "cleanupTestCase called");
            m_testCleaned = true;
            cleanup(m_provider, m_stack);
        }


        /**
         * General purpose cleanup method.
         * @param provider
         * @param stack
         * @exception jain.protocol.ss7.SS7Exception
         */
        protected synchronized void cleanup(JainTcapProvider provider,
                                            JainTcapStack stack) {
            m_isStarted = false;

            try {
                DEBUG_TRACE(this, "cleanup called");
                if (provider == null) {
                    DEBUG_TRACE(this, "****cleanup: provider is null.");
                    return;
                }
                try {

                    provider.removeTcapEventListener(this);
                    DEBUG_TRACE(this, "removeJainTcapListener returned");
                } catch (jain.protocol.ss7.SS7ListenerNotRegisteredException ex) {
                    DEBUG_TRACE(this, "****cleanup: listener not registered");
                } catch (VendorException ex) {
                    DEBUG_TRACE(this, "****cleanup: Error removing listener");
                }

                if (stack == null) {
                    DEBUG_TRACE(this, "****cleanup: stack is null.");
                    return;
                }
                //delete JainTcapProvider, delete will do detach also
                stack.deleteProvider(provider);
                DEBUG_TRACE(this, "deleteProvider returned");

            } catch (jain.protocol.ss7.SS7Exception ex) {
                error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(ex));
                return;
            }
        }


        /**
         * Called when error in listener.
         *
         * @param errorStr The description of the error.
         */
        public synchronized void error(String errorStr) {
            m_error += "\n" + errorStr;
            m_testFailed = true;
            this.notify();
        }



        /**
         *
         * @param invokeReq
         * @param dialogueReq
         * @param vbEx
         */
        protected void handleWouldBlock(EventObject currentReq,
                                        WouldBlockException vbEx) {
            int dialogueId = getDialogueId(currentReq);
System.out.println("handleWouldBlock: " + dialogueId);
            m_dialogueMgr.inactivate(m_dialogueMgr.lookUpDialogue(dialogueId));
            //release dialogueId
            m_provider.releaseDialogueId(dialogueId);

            beginDialogue();
        }

        /**
         *
         * @param invokeReq
         * @param dialogueReq
         * @param hdEx
         */
        protected
            void handleOutOfServiceException(EventObject currentReq,
                                             OutOfServiceException hdEx) {
            //In a real application the dialogue would probably be thrown away
            //The end user have to try again later
            m_isOutOfService = true;
            if (currentReq == null) {
                return;
            }

            //release dialogue
            int dialogueId = -1;
            try {
                dialogueId = getDialogueId(currentReq);
            } catch (Exception exception) {
                throw new RuntimeException(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(exception));
            }
            System.out.println("handleOutOfServiceException: " + dialogueId);

            m_dialogueMgr.inactivate(m_dialogueMgr.lookUpDialogue(dialogueId));
            //release dialogueId
            m_provider.releaseDialogueId(dialogueId);

            beginDialogue();
        }

        protected int getDialogueId(EventObject currentReq) {
            //release dialogue
            int dialogueId;
            try {
                if (currentReq instanceof VendorDialogueReqEvent) {
                    dialogueId =
                        ((VendorDialogueReqEvent)currentReq).getDialogueId();
                } else if (currentReq instanceof ComponentReqEvent) {
                    dialogueId =
                        ((ComponentReqEvent)currentReq).getDialogueId();
                } else {
                    dialogueId =
                        ((DialogueReqEvent)currentReq).getDialogueId();
                }
            } catch (Exception exception) {
                throw new RuntimeException(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(exception));
            }

            return dialogueId;
        }
    } //class Listener1000



    /**
     *The listener on the A side.
     */
    protected class ListenerA1000 extends Listener1000 {
        public ListenerA1000(TcapUserAddress origAddr, TcapUserAddress destAddr)
        {
            super(origAddr, destAddr);
        }

        public String toStringWithAdresses() {
            return new String ("originAddres: " + m_origAddr.toString() +
                " DestAddres: " + m_destAddr.toString());
        }


        /**
         * Process a non-JAIN event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public void processTcStateIndEvent(TcStateIndEvent event) {
            DEBUG_TRACE(this, "processTcStateIndEvent");
            if (m_isStarted) {
                if (!isReadyForTraffic(event, m_destAddr)) {
                    DEBUG_TRACE(this,
                                "processTcStateIndEvent congestion: " +
                                event.getUserStatus() + "\n" +
                                event.toString());
                    //@todo handle congestion...

                } else if (m_isOutOfService) {
                    DEBUG_TRACE(this,
                                "processTcStateIndEvent HD in service: " +
                                event.getUserStatus() + "\n" +
                                event.toString());
                    m_isOutOfService = false;
                    beginDialogue(); //re-start dialogue sending
                } else {
                    //ignore duplicates
                }

            } else { //!isStarted
                if (isReadyForTraffic(event, m_destAddr)) {
                    DEBUG_TRACE(this,
                                "readyForTraffic");
                    //m_destAddr is ready, start application...
                    m_isStarted = true;
                    startAppl();
                }
            }


        }// processTcStateIndEvent

        /**
         * Clean the A side after END_ind
         */
        private void cleanAfterEnd(ResultIndEvent event)
        throws SS7Exception {
            //release dialogue
            m_dialogueMgr.inactivate(m_dialogueMgr.
                                     lookUpDialogue(event.getDialogueId()));
            //release dialogueId
            m_provider.releaseDialogueId(event.getDialogueId());
        }



        /**
         *
         * @param event
         *
         * @exception SS7Exception
         */
        public void processResultIndEvent(ResultIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "ResultIndEvent received.");
            switch (m_stack.getProtocolVersion()) {
            case DialogueConstants.PROTOCOL_VERSION_ANSI_92:
            case DialogueConstants.PROTOCOL_VERSION_ANSI_96:
                m_provider.releaseInvokeId(event.getLinkId(),
                                           event.getDialogueId());
                break;
            case DialogueConstants.PROTOCOL_VERSION_ITU_93:
            case DialogueConstants.PROTOCOL_VERSION_ITU_97:
                m_provider.releaseInvokeId(event.getInvokeId(),
                                           event.getDialogueId());
                break;
            default:
                throw new RuntimeException("Wrong protocol version:\n");
            }
            Dialogue dialogue =
                m_dialogueMgr.lookUpDialogue(event.getDialogueId());
            if(dialogue == null) {
            	System.out.print("BBTC1000::processResultIndEvent(...) " +
                        "dialogue == null");
            	return;
            }

            m_numberOfInvokesMade++;

            if (dialogue.isEndReceived()) {
                //handle END_ind related result

                if (++m_dialogsReady < m_bbTestDialogues) {
                    if (m_dialogsReady % 1000 == 0) {
                        System.out.print("\nWorking ... ");
                        printTestHeader();
                        System.out.print("m_dialogsReady: " +
                                         m_dialogsReady);
                    }
                    //clean for the next dialogue
                    cleanAfterEnd(event);
                    beginDialogue();
                } else {
                    //clean before terminating test
                    cleanAfterEnd(event);
                    //wake up main thread
                    synchronized(this) {
                        this.notify();
                        DEBUG_TRACE(this,
                                    "this.notify in processResultIndEvent returned");
                    }
                }

            } else {
                //handle CONTINUE_ind related result

                this.sendInvokeAndContinue(event.getDialogueId());
            }
        }


        /**
         *
         */
        public void processContinueIndEvent(ContinueIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "Continue IndEvent received.");

            if (!event.isComponentsPresent()) {
                error("Wrong in A side processContinueIndEvent: " +
                      " isComponentsPresent returned false");
            }
        }


        /**
         *
         */
        public void processEndIndEvent(EndIndEvent event)
        throws SS7Exception {
            DEBUG_TRACE(this, "EndIndEvent received.");

            int dialogueId = event.getDialogueId();
            DEBUG_TRACE(this, "Dialogue ID " + dialogueId);

            Dialogue dialogue =
                m_dialogueMgr.lookUpDialogue(dialogueId);
            DEBUG_TRACE(this, "Current Dialogue " + dialogue);
            if(dialogue != null)
              dialogue.setEndReceived(true);
            else
            	System.out.println("Dialog: " + dialogueId + "was removed");
        }

        /**
         *  Component event.
         */
        public void processRejectUserIndEvent(RejectIndEvent event)
        throws SS7Exception {
            DEBUG_TRACE(this, "RejectUserIndEvent received.");
        }

        /**
         *
         */
        public void processProviderAbortIndEvent(ProviderAbortIndEvent event)
        throws SS7Exception {
            //relaese the dialogue ID
            int dialogueId = event.getDialogueId();
            Dialogue dialogue = m_dialogueMgr.lookUpDialogue(dialogueId);
            System.out.println("processProviderAbortIndEvent: " + dialogueId);

            if (dialogue!=null) {
                m_dialogueMgr.inactivate
                    (m_dialogueMgr.lookUpDialogue(dialogueId));
            }
            m_provider.releaseDialogueId(dialogueId);
            switch (event.getPAbort()) {
            case DialogueConstants.P_ABORT_IS_BLOCKED: // Ericsson specific
                //retry with a new dialogue ID,
                //due to blocked dialogue ID, but only if there are no active
                //dialogues
                if (!m_dialogueMgr.isDialogueLeft()) {
                    beginDialogue();
                }
                break;
            default:
                DEBUG_TRACE(this, "ProviderAbortIndEvent received.");
                error("ProviderAbortIndEvent received.");
            }

        }

        /**
         * Dialogue event.
         */
        public void processNoticeIndEvent(NoticeIndEvent event)
        throws SS7Exception {
            //NOTE: This method uses the dialogueId as relatedDialogueId
            //which is WRONG, but it works since there is a bug in J-TCAP
            //that places the relatedDialogueId in the dialogueId.
            //The returnIndicator, relatedDialogueId and
            //segmentationIndicator is missing in the JAIN TCAP NoticeIndEvent.

            DEBUG_TRACE(this, "processNoticeIndEvent");
            //cleanup
            int dialogueId = event.getDialogueId();
            System.out.println("processNoticeIndEvent: " + dialogueId);

            m_dialogueMgr.inactivate(m_dialogueMgr.
                                     lookUpDialogue(dialogueId));

            //SCCP Reason for return value:
            final byte receiveInactivityTimeout = 0x0D;

            if (event.getReportCause()[0] == receiveInactivityTimeout) {
                DEBUG_TRACE(this, "processNoticeIndEvent reportCause " +
                            "receiveInactivityTimeout, value: " +
                            event.getReportCause()[0]);
                //assume that B side has recoverd:
                m_provider.releaseDialogueId(dialogueId);
                beginDialogue();
            } else {
                error("processNoticeIndEvent reportCause: " +
                      event.getReportCause()[0]);
            }
        }


        /**
         * Return the operation code to send to B-side.
         * @return The operation code.
         */
        public byte[] getInvokeOperationCode() {

            //check which stage in test we are in
            if (m_dialogsReady >= m_bbTestDialogues) {
                return OPERATION_END_LAST_DIALOGUE;
            }
            if (m_numberOfInvokesMade >= (getMaxNumberOfInvokes() - 1)) {
                return OPERATION_END_DIALOGUE;
            } else {
                return OPERATION_CONTINUE_DIALOGUE;
            }
        }


        /**
         * Get number of invokes for each dialogue
         * @return Max number of invokes that shall be made.
         */
        private int getMaxNumberOfInvokes() {
            //according to primitive sequence at top of this file
            return m_maxNumberOfInvokes;
        }


        /**
         * Set max number of invokes that shall be made for each dialogue.
         * Call this if more then default number of invokes shall be
         * made for each dialogue.
         * @param numberOfInvokes Number of invokes for each dialogue
         */
        protected void setMaxNumberOfInvokes(int numberOfInvokes) {
            //according to primitive sequence at top of this file
            m_maxNumberOfInvokes = numberOfInvokes;
        }


        /**
         * Build an invoke and continue request, then send them.
         *
         * @param dialogueId The dialogue to continue with.
         */
        private void sendInvokeAndContinue(int dialogueId) {
            InvokeReqEvent invokeReq = null;
            ContinueReqEvent continueReq = null;
            EventObject currentReq = null;
            try {
                int invokeId=m_provider.getNewInvokeId(dialogueId);
                currentReq = invokeReq =
                    createInvokeReq(this,
                                    invokeId,
                                    getInvokeOperationCode(),
                                    true); //with paramters

                invokeReq.setDialogueId(dialogueId);
                invokeReq.setTimeOut(BB_TEST_INVOKE_TIMEOUT);
                //set class 1 operation (report failure or success)
                invokeReq.setClassType(ComponentConstants.CLASS_1);
                DEBUG_TRACE(this, "Sending invoke with invokeID: "
                            + invokeId + "  ...");

                m_provider.sendComponentReqEventNB(invokeReq);


                //----- Build begin request:

                currentReq = continueReq = createContinueReq(this,
                                                             dialogueId);
                DEBUG_TRACE(this, "Sending continue...");
                m_provider.sendDialogueReqEventNB(continueReq);
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
    } //end class Listener A1000



    /**
     *The listener on the B side (not used if B side is a test stub).
     */
    protected class ListenerB1000 extends Listener1000 {
        /**
         *
         */
        public ListenerB1000(TcapUserAddress origAddr, TcapUserAddress destAddr)
        {
            super(origAddr, destAddr);
        }

        public String toStringWithAdresses() {
            return new String ("originAddres: " + m_origAddr.toString() +
                " DestAddres: " + m_destAddr.toString());
        }

        /**
         *
         */
        protected void startAppl() {
            System.out.println("\n\nWaiting for TCAP indication ...");
        }

        /**
         *
         */
        public void processInvokeIndEvent(InvokeIndEvent event)
        throws SS7Exception, VendorException {

            EventObject currentReq = null;
            boolean isLastDialogue = false;
            int dialogueId = -1;
            try {
                DEBUG_TRACE(this, "InvokeIndEvent received.");
                InvokeIndEvent invokeInd = (InvokeIndEvent)event;

                //----- Build result request:
                dialogueId = invokeInd.getDialogueId();
                ResultReqEvent resultReq = null;
                currentReq = resultReq = createResultReq(this,
                                                         dialogueId,
                                                         event.getInvokeId(),
                                                         true);
                resultReq.setInvokeId(invokeInd.getInvokeId());


                DEBUG_TRACE(this, "Sending result...");
                m_provider.sendComponentReqEventNB(resultReq);

                //---Build continue or end request, depending on operation code

                DialogueReqEvent dialogueReq = null;
                DEBUG_TRACE(this,event.toString());
                byte[] operation = event.getOperation().getOperationCode();

                if (operation[0] == OPERATION_CONTINUE_DIALOGUE[0]) {
                    currentReq = dialogueReq =
                        createContinueReq(this, dialogueId);
                } else if (operation[0] == OPERATION_END_DIALOGUE[0]) {
                    currentReq = dialogueReq = createEndReq(this, dialogueId);
                    m_dialogsReady++;
                } else if (operation[0] == OPERATION_END_LAST_DIALOGUE[0]) {
                    currentReq = dialogueReq = createEndReq(this, dialogueId);
                    isLastDialogue = true;
                    m_dialogsReady++;
                } else if (operation[0] == ATI[0]) {
                    currentReq = dialogueReq = createEndReq(this, dialogueId);
                    isLastDialogue = true;
                    m_dialogsReady++;
                    DEBUG_TRACE(this, "Got ATI");
                } else {
                    DEBUG_TRACE(this, "Unexpected Operation...");
                    error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace
                          ( new RuntimeException("Unexpected operation code: " +
                                                 operation[0])));
                }
                DEBUG_TRACE(this, "Sending continue or end...");
                m_provider.sendDialogueReqEventNB(dialogueReq);
            } catch (WouldBlockException vbEx) {
                handleWouldBlock(currentReq, vbEx);
            } catch (OutOfServiceException oosEx) {
                handleOutOfServiceException(currentReq, oosEx);
            } catch (VendorException vEx) {
                vEx.printStackTrace();
                throw vEx;
            }

            if (isLastDialogue) {
                //wake up main thread
                synchronized(this) {
                    this.notify();
                }
            }
        }


        /**
         *
         */
        public void processBeginIndEvent(BeginIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "BeginIndEvent received.");
        }


        /**
         *
         */
        public void processContinueIndEvent(ContinueIndEvent event)
        throws SS7Exception {

            DEBUG_TRACE(this, "Continue IndEvent received.");

        }


    } // end ListenerB1000


    /**
     * Data class for a dialogue.
     */
    protected class Dialogue {

    	private Listener1000 m_listener;
        private int m_dialogueId;
        private int m_ssn;
        private JainTcapProvider m_provider;
        private boolean m_isEndReceived = false;
        TestErrorHandler m_errHandl;

        /**
         * Dialogue constructor.
         * @param listener
         * @param dialogueId
         * @param ssn
         * @param provider
         * @param errHandl
         */
        public Dialogue(Listener1000 listener,
                        int dialogueId,
                        int ssn,
                        JainTcapProvider provider,
                        TestErrorHandler errHandl) {

            m_listener = listener;
            m_dialogueId = dialogueId;
            m_ssn = ssn;
            m_provider = provider;
            m_errHandl = errHandl;
        }

        public void cleanUp() {
            //...
        }

        public int getSsn() {
            return m_ssn;
        }

        public int getDialogueId() {
            return m_dialogueId;
        }

        public JainTcapProvider getJainTcapProvider() {
            return m_provider;
        }

        /**
         * Handle a non-JAIN dialogue event (Ericsson Specific event).
         * @param event The indication event that is going to be processed.
         */
        public void handleVendorDialogueIndEvent(
        		VendorDialogueIndEvent event) {

            int primitive = event.getPrimitiveType();
            switch (primitive) {
            case TcAddressIndEvent.PRIMITIVE_TC_ADDRESS_IND:
                handleTcAddressIndEvent((TcAddressIndEvent)event);
                break;
            default:
                m_errHandl.error(ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(
                		new RuntimeException("Unexpected primitive:\n" +
                                              event)));
            }
        }

        protected void handleTcAddressIndEvent(TcAddressIndEvent event) {
            DEBUG_TRACE(this, "handleTcAddressIndEvent");
            TcapUserAddress address = event.getAddress();
            int mask = event.getAddressBitMask();
            //do something with the address that was requested earlier...
            m_listener.setDestinationAddress(address);
        }

        public int hashCode() {
            return getDialogueId();
        }

        public boolean isEndReceived() {
            return m_isEndReceived;
        }

        public void setEndReceived(boolean val) {
            m_isEndReceived = val;
        }

        public boolean equals(Object o) {
            return this.hashCode() == o.hashCode();
        }

        public String toString() {
            return "[" + this.getClass().getName() + "{ dialogueId: " +
                m_dialogueId + " SSN: " + m_ssn + " Provider hashCode: " +
                m_provider.hashCode() + "]";
        }


    } //END class Dialogue


    /**
     * Keeps the active dialogues.
     */
    protected class DialogueManager {

        Map m_dialogueMap = java.util.Collections.synchronizedMap(new HashMap());
        TestErrorHandler m_errHandl;

        /**
         * Constructor
         * @param errorHandler
         */
        public DialogueManager(TestErrorHandler errorHandler) {
            m_errHandl = errorHandler;
        }

        /**
         * Activate a Dialogue.
         * @param dialogue a dialogue to activate
         */
        public void activate(Dialogue dialogue) {
            Dialogue old = (Dialogue) m_dialogueMap.put(dialogue,
                                                        dialogue);
            if (old != null) {
                m_dialogueMap.put(old,
                                  old);
                m_errHandl.error("Programming error, there was " +
                                 "already an active dialogue: " +
                                 old.toString() +
                                 " which would have been overwritten by: " +
                                 dialogue.toString());
            }
        }

        /**
         * Removes all dialogues.
         */
        public void clearAllDialogs() {
            //call clear on each Dialogue?
            m_dialogueMap.clear();
        }

        /**
         * Inactivate a Daialogue.
         * @param dialogue the dialogue to inactivate.
         */
        public void inactivate(Dialogue dialogue) {
            Dialogue old = (Dialogue) m_dialogueMap.remove(dialogue);
//System.out.println("inactivate: " + dialogue.getDialogueId());

            if (old == null) {
                m_errHandl.error("Programming error, there was " +
                                 "no active dialogue for: " +
                                 dialogue.toString());
            } else {
                old.cleanUp();
            }
        }

        /**
         * Clean lost dialogues
         * @param event
         */
        public void cleanUpLostDialogues(TcDialoguesLostIndEvent event) {
            Iterator iter = m_dialogueMap.values().iterator();
            Dialogue dialogue = null;
            while (iter.hasNext()) {
                dialogue = (Dialogue) iter.next();
                if (event.isDialogueIdLost(dialogue.getDialogueId())) {
                    inactivate(dialogue);
                }
            }
        }

        /**
         * Verify if there are alive dialogues.
         * @return true if dialogue map is not empty.
         */
        public boolean isDialogueLeft() {
            return !m_dialogueMap.isEmpty();
        }

        /**
         * Get a Dialogue object by dialogueID.
         * @param dialogueId a dialogueID
         * @return the Dialogue object
         */
        public Dialogue lookUpDialogue(int dialogueId) {
            return (Dialogue) m_dialogueMap.get(
            		new Dialogue(null,
                                 dialogueId,
                                 -1,
                                 null,
                                 null));
        }

        /**
         * Prints left dialogues.
         * @param stream PrintStream to write
         */
        public void printDialoguesLeft(PrintStream stream) {
            printDialoguesLeft(new PrintWriter(stream));
        }

        /**
         *
         * @param w
         */
        public void printDialoguesLeft(PrintWriter w) {
            Iterator iter = m_dialogueMap.values().iterator();
            Dialogue dialogue = null;
            while (iter.hasNext()) {
                dialogue = (Dialogue) iter.next();
                w.println(dialogue.toString());
            }
        }

    } //class DialogueManager
}



/**
 * Output from beginDialogue operation
 */
/*class OutBeginDialogue {
  int dialogueId;
  int invokeId;
  }*/



