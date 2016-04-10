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
package jtcapblackbox;


//java imports
import java.util.*;
import java.lang.reflect.*;

//ericsson imports
import com.ericsson.jain.protocol.ss7.tcap.JainTcapConfig;
import ericsson.ein.ss7.commonparts.CommonPartsFactory;
import ericsson.ein.ss7.commonparts.util.Tools;

//jain imports
import jain.protocol.ss7.JainSS7Factory;
import jain.protocol.ss7.tcap.*;
import jain.protocol.ss7.tcap.component.*;
import jain.protocol.ss7.tcap.dialogue.*;

/**
 * Used in JTCAP javadoc as an example, but is also part of Basic Test.
 * Run several test cases in a sequence (similar to JUnit but with
 * better control). There are different "modes" for the B-side:
 *   - B side is a test stub (no SS7 stack), this is used in
 *     Basic Test. The test stub overrides some internal classes in JCP.
 *   - One SS7 stack, using the same Point Code (SPC) for A and B side
 *     so that traffic is goes down to SCCP and back up to TCAP again.
 *   - Two SS7 stacks with A and B side on different stacks.
 * NOTE: The static part of TcapApiTest (main and other static parts)
 * is the "Test Suite". The "Test Suite" creates instances
 * of TcapApiTest sub-classes (by the use of reflection) named with
 * prefix BBTC (for example test case BBTC100 is a sub-class of
 * TcapApiTest).
 * See main-operation for a description of input parameters; or run
 * without any parameters to get description printed to console. The
 * makefile for JTCAP shows how to use the parameters and system
 * properties.
 * NOTE: Test cases 1-14 + gt are old and may not work, the new test cases
 * start at 100.
 * <P>
 * <h2>Testcase example usage</h2>
 * <P>
 * The below java files contain examples of how to use the J-TCAP:
 * </P>
 * <a href="com/ericsson/jain/protocol/ss7/tcap/doc-files/TcapApiTest.java">TcapApiTest.java</a>: Contains code used by the two applications in BBTC1000 and BBTC2000.
 *
 * <BR>
 * <a href="com/ericsson/jain/protocol/ss7/tcap/doc-files/BBTC1000.java">BBTC1000.java</a>: Contains a J-TCAP User application that uses file configuration and one TCAP BE.
 * <BR>
 * <a href="com/ericsson/jain/protocol/ss7/tcap/doc-files/BBTC2000.java">BBTC2000.java</a>: Contains a J-TCAP User application that uses CP Manager configuration and multiple TCAP BE.
 * <BR>
 * <a href="com/ericsson/jain/protocol/ss7/tcap/doc-files/OutBeginDialogue.java">OutBeginDialogue.java</a>:
 * Auxilary class. Contains parameters of dialogue being issued.
 * <BR>
 * <a href="com/ericsson/jain/protocol/ss7/tcap/doc-files/manifest">manifest</a>:
 * Contains library and class paths to launch BBTC1000 testcase manually.
 * <BR>
 *
 * <P>
 * <b>NOTE:</b> Follow instructions bellow to launch BBTC1000 (BBTC2000) tests
 * manually:
 *
 *
 * <ul>
 *   <li> Copy highlighted source *.java files (except manifest) to some subdirectory, for example <b>src</b>, of your work directory.
 *   <li> Copy archived module's files: einss7javacp.jar, eintcapapi.jar, einss7japi.jar, jaintcapapi.jar (or equivalent) and highlighted <b>manifest</b> to your work directory.
 *   <li> Create <b>classes</b> subdirectory in your work directory for output of compilation:  mkdir classes
 *   <li> Compile sources:  javac -classpath einss7javacp.jar:eintcapapi.jar:einss7japi.jar:jaintcapapi.jar src/*.java -d classes
 *   <li> Generate executable archive:  jar cvfm BBTC1000.jar ./manifest -C classes/ .
 *   <li> To launch BBTC1000 testcase manually, use the command:  java -jar BBTC1000.jar
 *   <li> *<tt>Edit <b>manifest</b> to run BBTC2000 testcase manually</tt>
 *
 * </ul>
 * <P>
 */
public class TcapApiTest {
    //NOTE: the SPC need to match those in
    //      the ss7.cnf or in the JCP stub

    private byte[] m_spcA = { //signaling point 2143
        7,  //id
        11, //area
        1,  //zone
    };

    private byte[] m_spcB = { //signaling point 3342
        6, //id
        Tools.getLoByteOf2(161), //area
        1, //zone
    };
    protected static boolean m_oneStack = false;
    protected TcapUserAddress m_userAddressA1;
    protected TcapUserAddress m_userAddressB1;
    protected TcapUserAddress m_userAddressA2;
    protected TcapUserAddress m_userAddressB2;
    protected int m_std = 0;
    protected static int m_stackSide;
    protected String m_testName;

    protected static boolean BB_TEST_TRACE =
        System.getProperty("BB_TEST_TRACE",
                           "off").equals("stdout");

    //Test Suite variables (must be static since the Test Suite is the
    //static part of TcapApiTest):
    private static String m_testSuiteErrors = "TestSuiteErrors:";
    private static boolean m_testSuiteFailed = false;
    private static String m_testSuiteWarnings = "TestSuiteWarnings:";
    private static boolean m_testSuiteWarning = false;

    static final byte[] HELLO_WORLD_OP = { 0x07};
    static final byte[] HELLO_ANSWER_OP = { 0x08};
    static final int SIDE_A = 1;
    static final int SIDE_B = 2;
    static final int PARAM_LEN = Integer.parseInt(System.getProperty("PARAM_LEN", "30"));


    //-------------------------------------------------------------------------
    public TcapApiTest(int std) {

        //set the addresses (must match those in ss7.cnf if a real stack is
        //used)
        if (m_oneStack) {

            //set DPC == OPC, but use different SSN
            m_userAddressA1 =
                new TcapUserAddress(m_spcA, (short)210);
            m_userAddressB1 =
                new TcapUserAddress(m_spcA,(short)250);
            m_userAddressA2 =
                new TcapUserAddress(m_spcA,(short)200);
            m_userAddressB2 =
                new TcapUserAddress(m_spcA,(short)205);
        } else {
            m_userAddressA1 =
                new TcapUserAddress(m_spcA, (short)210);
            m_userAddressB1 =
                new TcapUserAddress(m_spcB,(short)210);
            m_userAddressA2 =
                new TcapUserAddress(m_spcA,(short)250);
            m_userAddressB2 =
                new TcapUserAddress(m_spcB,(short)250);
        }

        m_std = std;
    }

    public void executeA() {
        System.out.println("Not implemented.");
    }

    public void executeB() {
        System.out.println("Not implemented.");
    }

    //-------------------------------------------------------------------------
    public void recoverProvider(JainTcapStack stack, JainTcapProvider provider ) {
        System.out.println("Not implemented.");
    }

    //-------------------------------------------------------------------------
    public static String getHelp() {
        return "Parameters: {Side of stack (A/B)} {TestCases (3,5-7,9)}" +
            " {standard (a, i93, i97)}" +
            " [One or two stacks, default two (onestack)]" +
            " [Use JCP test stub, default off (stub)]" +
            " [-repeatSeq {number of repeats, default 1} (-repeatSeq 100)]";
    }


    //-------------------------------------------------------------------------
    /**
     * Parses strings like "3,5-7,9" and returns all numbers in array.
     *
     * @param parseString
     *                  String to parse.
     * @param capacity The maximum number of integers in result.
     * @param isTraceOn If should trace the progress or not.
     * @return The array of int.
     */
    protected static int[] intervalToIntegers(String parseString,
                                              int capacity,
                                              final boolean isTraceOn) {

        int[] testA = new int[capacity];

        StringTokenizer st =
            new StringTokenizer(parseString,
                                ",-",
                                true) { //return delimeters
            //override to produce trace
            public String nextToken() {
                String token = super.nextToken();
                if (isTraceOn) {
                    System.out.print(token);
                }
                return token;
            }
        };//end anonymous class

        if (isTraceOn) {
            System.out.print("\nParsing... ");
        }
        int testNum = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals(",")) {
                //next token is a single integer or the first in a range
                testA[testNum++] = Integer.parseInt(st.nextToken());
            } else if (token.equals("-")) {
                //next token is the end of the range
                int stop = Integer.parseInt(st.nextToken());
                //start add after the previous added number
                int tmp = testA[testNum-1] + 1;
                while (tmp <= stop) {
                    testA[testNum++] = tmp++;
                }
            } else {
                //the starting integer
                testA[testNum++] =  Integer.parseInt(token);
            }

        }
        if (isTraceOn) {
            System.out.println(" ...done parsing.");
        }

        //trim away the unused part of the array
        int[] tmp = new int[testNum];
        System.arraycopy(testA, 0,
                         tmp, 0,
                         testNum);
        return tmp;
    }

    /**
     *
     * @param args See getHelp operation.
     */
    public static void main(String[] args) {

        if (args.length < 3) {
            throw new RuntimeException("Missing parameters. \n" + getHelp());
        }

        int std = 0;
        if (BB_TEST_TRACE) System.out.println("arg[0] " + args[0]);
        if (BB_TEST_TRACE) System.out.println("arg[1] " + args[1]);
        if (BB_TEST_TRACE) System.out.println("arg[2] " + args[2]);
        if ((args[2].equals("a")) || (args[2].equals("A"))) {
            std = DialogueConstants.PROTOCOL_VERSION_ANSI_96;
            if (BB_TEST_TRACE) System.out.println("ANSI 96");
        } else if ((args[2].equals("i93")) || (args[2].equals("I93"))) {
            std = DialogueConstants.PROTOCOL_VERSION_ITU_93;
            if (BB_TEST_TRACE) System.out.println("ITU 93");
        } else {
            if (BB_TEST_TRACE) System.out.println("ITU 97");
            std = DialogueConstants.PROTOCOL_VERSION_ITU_97;
        }

        if (args.length > 3 && args[3].equals("onestack")) {
            if (BB_TEST_TRACE) System.out.println(args[3] + " set to true");
            m_oneStack = true;
        }

        if (args.length > 4 && args[4].equals("jcpstub")) {
            //Module test only, not needed when there is a TCAP stack.

            if (BB_TEST_TRACE) System.out.println(args[4] + " set to true");

            //The module test uses a test stub instead of JCP, so we need
            //to change factory.
            //Change of factory can only be made once, so it cannot
            //be made in each testcase (which also means that it is not
            //possible to mix stub test cases with real stack test cases)

            try {
                //Create test stub from the class string, since the stub is not
                //available for customers:
                Class stubCpFactoryClass =
                    Class.forName("ericsson.ein.ss7.commonparts.StubCpFactory");

                CommonPartsFactory.setFactory((CommonPartsFactory)
                                              stubCpFactoryClass.newInstance());

            } catch (Exception ex) {
                throw new RuntimeException("Test stub not available! \n" +
                                           Tools.getStackTrace(ex));
            }

        }

        int[] tests = intervalToIntegers(args[1],
                                         200,
                                         BB_TEST_TRACE);

        int repeatSequence = 1;
        //run the specified test cases
        if (args.length > 6 && args[5].equals("-repeatSeq")) {
            repeatSequence = Integer.parseInt(args[6]);
        }
        int numOfTestCases = 0;
        long startTime = System.currentTimeMillis();
        for (int j=0; j<repeatSequence; j++) {
            for (int i=0; i<tests.length; i++) {
                resetTestContext(args.length > 4 && args[4].equals("jcpstub"));
                runTestCase(tests[i], std, args[0]);
                numOfTestCases++;
            }
        }
        long time = System.currentTimeMillis() - startTime;
        double timeInSec = (float)time/1000;
        System.out.println("\nTime: " + timeInSec + "\n");

        if (m_testSuiteFailed) {
            System.out.println("\n****TEST SUITE FAILED!!! \n" +
                               m_testSuiteErrors +
                               "\nEND TEST SUITE ERRORS.\n");
        } else {
            if (m_testSuiteWarning) {
                System.out.println("\n****TEST SUITE WARNINGS! \n" +
                                   m_testSuiteWarnings +
                                   "\nEND TEST SUITE WARNINGS.\n");
            }
            System.out.println("\nTEST SUITE OK (" + numOfTestCases +
                               " tests)\n");
        }
    }


    /**
     * Reset test context, should be made between each test case.
     */
    private static void resetTestContext(boolean stubIsUsed) {
        
            //Also used as example for customers.
            //The customer does not have the test stub, so use reflection to
            //call StubCpMsgProvider.clearStates():
            if (stubIsUsed) {
                try {
                Class stubCpMsgProviderClass =
                        Class.forName("ericsson.ein.ss7.commonparts.StubCpMsgProvider");

                    System.out.println("stubCpMsgProviderClass Loaded: " + stubCpMsgProviderClass);

                    Method clearStatesMethod =
                        stubCpMsgProviderClass.getMethod("clearStates", new Class[]{});
                    clearStatesMethod.invoke(null, new Object[]{});

                } catch (Exception ex) {
                    throw new RuntimeException("Test stub not available! \n" +
                                               Tools.getStackTrace(ex));
                }
            }

        JainTcapConfig.getInstance().clearConfig();
        System.setProperty("STUB_JCP_STATE", "CLEARED");
        System.setProperty("STUB_JCP_STATE_1", "CLEARED");
        System.setProperty("STUB_JCP_STATE_2", "CLEARED");

        //Clear to highest interface version.
        //Use a high value to let T_BIND_req control which version to use
        //since the smallest value is handshaked.
        System.setProperty("STUB_JCP_IF_VERSION", "1000000");
        System.setProperty("STUB_JCP_IF_VERSION_1", "1000000");
        System.setProperty("STUB_JCP_IF_VERSION_2", "1000000");

    }

    /**
     *
     * @param number
     * @param std
     */
    private static void runTestCase(int number, int std, String stack) {
        String numberString = Integer.toString(number);
        TcapApiTest tcTest = null;


        if (number < 10) {
            //fix that the classes are named BBTC01 instead of BBTC1
            numberString = "0" + numberString;
        }

        //Mark the internal threads in JCP with test case number, just in case
        //test cases start to leak into each other, then it can be seen in
        //the JSwat debugger.
        System.setProperty("ericsson.ein.ss7.commonparts.PROPERTY_PROVIDER",
                           numberString + stack);

        try {
            //User java reflection to create test case instance:
            System.out.println("jtcapblackbox.BBTC" + numberString + " Starts");
            Class tcClass = Class.forName("jtcapblackbox.BBTC" + numberString);
            Class param[] = { int.class};
            Object actParam[] = { new Integer(std)};
            Constructor tcConstructor = tcClass.getConstructor(param);

            tcTest = (TcapApiTest)tcConstructor.newInstance(actParam);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }


        if (stack.equals("A")) {
            m_stackSide = SIDE_A;
            tcTest.executeA();
        } else if (stack.equals("B")) {
            m_stackSide = SIDE_A;
            tcTest.executeB();
        } else {
            throw new RuntimeException("Unknown stack: " + stack +
                                       "\n" + getHelp());
        }
    }

    public InvokeReqEvent createInvokeReq (java.lang.Object source,
                                           int invokeId,
                                           byte[] opData) {
        return createInvokeReq(source,invokeId,opData,false);
    }


    public InvokeReqEvent createInvokeReq (java.lang.Object source,
                                           int invokeId,
                                           byte[] opData,
                                           boolean withparams  ) {

        return createInvokeReq(source, invokeId, opData, withparams, PARAM_LEN, false);
    }

    public InvokeReqEvent createInvokeReq (java.lang.Object source,
                                           int invokeId,
                                           byte[] opData,
                                           boolean withparams,
                                           int paramLen,
                                           boolean isRawParam) {

        InvokeReqEvent invokeReq = new InvokeReqEvent(source,invokeId,null);
        invokeReq.setInvokeId(invokeId);
        invokeReq.setLastInvokeEvent(true);
        invokeReq.setTimeOut(100000);
        invokeReq.setClassType(ComponentConstants.CLASS_3);
        Operation op = new Operation(); //FIX: Operation class, CODE to TYPE in constants
        op.setOperationCode(opData);
        op.setPrivateOperationData(opData);
        op.setOperationFamily(Operation.OPERATIONFAMILY_PROCEDURAL);
        op.setOperationSpecifier(Operation.CHARGINGSPECIFIER_BILLCALL);
        op.setOperationType(Operation.OPERATIONTYPE_GLOBAL); //FIX: code vs. type missunderstanding
        invokeReq.setOperation(op);
        if (withparams) {
            byte[] params= createParameters(paramLen, isRawParam);

            invokeReq.setParameters(new Parameters(Parameters.PARAMETERTYPE_SEQUENCE,
                                                   params));
        }
        return invokeReq;
    }

    protected byte[] createParameters(int paramLength, boolean isRaw) {

        byte[] params= new byte[paramLength];
        int pos = 0;
        if (isRaw) {
            params[pos++] = Tools.getLoByteOf2(0x30); //sequence tag
            pos = setLength(paramLength, params, pos); //total length
        }
        params[pos++] = Tools.getLoByteOf2(0x04); //OCTETSTRING id (ASN.1)
        pos = setLength(paramLength-pos-1, params, pos); //length of OCTETSTRING
        for (int i=pos; i<paramLength-pos; i++) {
            //fill up with dummy data
            params[i] = Tools.getLoByteOf2(i-pos);
        }
        return params;
    }

    protected int setLength(int paramLength, byte[] params, int pos) {
        if (paramLength <= 0x7F) {
            params[pos++] = Tools.getLoByteOf2(paramLength);
        } else if (paramLength <= 0xFF) {
            params[pos++] = Tools.getLoByteOf2(0x81); //length tag
            params[pos++] = Tools.getLoByteOf2(paramLength);
        } else {
            params[pos++] = Tools.getLoByteOf2(0x82); //length tag
            params[pos++] = Tools.getLoByteOf2(paramLength);
            params[pos++] = Tools.getHiByteOf2(paramLength);
        }
        return pos;
    }


    public ResultReqEvent createResultReq (java.lang.Object source,
                                           int dialogId,
                                           int invokeId,
                                           boolean lastResult) {

        ResultReqEvent resReq = new ResultReqEvent(source,dialogId,lastResult);
        resReq.setInvokeId(invokeId);
        return resReq;
    }

    public BeginReqEvent createBeginReq (java.lang.Object source,
                                         int dialogueId,
                                         TcapUserAddress origAddr,
                                         TcapUserAddress destAddr ) {

        BeginReqEvent beginReq = new BeginReqEvent(source,dialogueId,origAddr,destAddr);
        beginReq.setQualityOfService((byte)2); //FIX: constant for qos in JAIN
        beginReq.setOriginatingAddress(origAddr);
        beginReq.setDestinationAddress(destAddr);
        beginReq.setDialoguePortion(new DialoguePortion());
        return beginReq;
    }

    public ContinueReqEvent createContinueReq (java.lang.Object source,
                                               int dialogueId ) {

        ContinueReqEvent endReq = new ContinueReqEvent(source,0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte)2); //FIX: constant for qos in JAIN
        endReq.setDialoguePortion(new DialoguePortion());
        return endReq;
    }

    public static EndReqEvent createEndReq (java.lang.Object source, int dialogueId ) {

        EndReqEvent endReq = new EndReqEvent(source,0);
        endReq.setDialogueId(dialogueId);
        endReq.setQualityOfService((byte)2); //FIX: constant for qos in JAIN
        endReq.setTermination(DialogueConstants.TC_BASIC_END);
        endReq.setDialoguePortion(new DialoguePortion());

        return endReq;
    }

    public JainTcapStack bindToStack(int std)
    throws
        jain.protocol.ss7.SS7PeerUnavailableException,
        jain.protocol.ss7.tcap.TcapException {

        JainTcapProvider provider = null;

        JainSS7Factory.getInstance().setPathName("com.ericsson");
        JainTcapStack stack = (JainTcapStack) JainSS7Factory.
            getInstance().createSS7Object("jain.protocol.ss7.tcap.JainTcapStackImpl");
        stack.setProtocolVersion(std);
        stack.setStackName(m_testName);
        return stack;
    }


    //a better name for the bindToStack operation
    public JainTcapStack createJainTcapStack(int std)
    throws
        jain.protocol.ss7.SS7PeerUnavailableException,
        jain.protocol.ss7.tcap.TcapException
    {
        return bindToStack(std);
    }


    public boolean checkOperationCode(InvokeIndEvent invokeInd, byte opCode)
    throws jain.protocol.ss7.tcap.ParameterNotSetException {

        boolean isSuccess = false;
        byte [] operationCode = null;
        try {
            operationCode=invokeInd.getOperation().getOperationCode();
        } catch (ParameterNotSetException e) {
            operationCode=invokeInd.getOperation().getPrivateOperationData();
        }

        if (operationCode[0] == opCode) {
            isSuccess = true;
        }

        return isSuccess;
    }

    public static void testSuiteError(String error) {
        m_testSuiteErrors += "\n" + error;
        m_testSuiteFailed = true;
    }
    public static void testSuiteWarning(String warning) {
        m_testSuiteWarnings += "\n" + warning;
        m_testSuiteWarning = true;
    }

    //NOTE: all class member fields are at the top of the class!
}


