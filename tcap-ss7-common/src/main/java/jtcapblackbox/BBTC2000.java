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
 * TestName: HD Example: CP Manager config and multiple BE.
 * <P>
 * Used in JTCAP javadoc as an example of multi BE (BackEnd). The sequence of
 * TCAP primitives is the same as for BBTC1000, except that the dialoges are
 * distributed over two TCAP BE process. Only the configuration differs
 * from the BBTC1000.
 *
 */
public class BBTC2000 extends BBTC1000 {

    public BBTC2000(int std) {
        super(std);
    }

    //inherit javadoc
    int getExpectedTcBindIndEvents() {
         return 2;
    }

    public String getTestName() {
        return "[HD Example: CP Manager config and multiple BE  ]";
    }

    /**
     * Set configuration properties.
     */
    protected void setConfiguration() {

        try {
            //NOTE: a real application would NOT hard code the
            //configString as in this example, get the configString fro
            //some data base or file instead:
            String configString =
                "-DEIN_JCP_CPMANAGER_ADDRESS=localhost:11111 " +
                "-DEIN_JCP_USER_ID=USER02_ID " +
                "-DEIN_HD_USER_INSTANCE=7 " +
                "-DEIN_HD_ATTACH_INSTANCES=1,2 " +
                "-DEINSS7_DID_SLICING_ID=1 " +
                "-DEINSS7_DID_NUM_OF_SLICES=2 ";

            JainTcapConfig.getInstance().setConfigString(configString);

        } catch (VendorException vex) {
            throw new RuntimeException("Error in config\n" +
            		ericsson.ein.ss7.commonparts.util.Tools.getStackTrace(vex));
        }
    }
}

