package com.vennetics.bell.sam.ss7.tcap.common.component.requests;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vennetics.bell.sam.ss7.tcap.common.component.requests.AbstractComponentRequestBuilder;
import com.vennetics.bell.sam.ss7.tcap.common.dialogue.TcapComponent;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.ISs7ConfigurationProperties;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

import ericsson.ein.ss7.commonparts.util.Tools;
import jain.protocol.ss7.tcap.component.ComponentConstants;
import jain.protocol.ss7.tcap.component.InvokeReqEvent;
import jain.protocol.ss7.tcap.component.Operation;
import jain.protocol.ss7.tcap.component.Parameters;

@Component
public class ItuComponentRequestBuilder extends AbstractTcapComponentRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ItuComponentRequestBuilder.class);
    
    ItuComponentRequestBuilder() {
        super();
        logger.debug("Constructed ATIComponentRequestBuilder");
    }
    
    @Override
    public InvokeReqEvent createInvokeReq(final Object source,
                                          final TcapComponent component,
                                          final ISs7ConfigurationProperties configProps,
                                          final int dialogueId) {
        final InvokeReqEvent invokeReq = new InvokeReqEvent(source,
                                                            component.getInternalInvokeId(),
                                                            null);
        invokeReq.setDialogueId(dialogueId);
        invokeReq.setLastInvokeEvent(true);
        invokeReq.setTimeOut(configProps.getInvokeTimeout());
        // set class 1 operation (report failure or success i.e timeout is an abnormal failure)
        invokeReq.setClassType(ComponentConstants.CLASS_1);
        final Operation op = new Operation();
        op.setOperationCode(EncodingHelper.byteToByte(component.getOperation()));
        op.setPrivateOperationData(EncodingHelper.byteToByte(component.getOperation()));
        op.setOperationType(Operation.OPERATIONTYPE_LOCAL);
        invokeReq.setOperation(op);
        invokeReq.setParameters(new Parameters(Parameters.PARAMETERTYPE_SEQUENCE, EncodingHelper.byteToByte(component.getParams())));
        return invokeReq;
    }
   
}
