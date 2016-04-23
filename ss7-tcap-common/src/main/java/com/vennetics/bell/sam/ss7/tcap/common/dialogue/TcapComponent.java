package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vennetics.bell.sam.ss7.tcap.common.utils.EncodingHelper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TcapComponent {

    private final Integer externalInvokeId;
    private final Integer internalInvokeId;
    private final Byte[] operation;
    private final Byte[] params;
    
    TcapComponent(@JsonProperty("externalInvokeId") final Integer externalInvokeId,
                  @JsonProperty("internalInvokeId") final Integer internalInvokeId,
                  @JsonProperty("internalInvokeId") final Byte[] operation,
                  @JsonProperty("internalInvokeId") final Byte[] params) {
        this.externalInvokeId = externalInvokeId;
        this.internalInvokeId = internalInvokeId;
        this.operation = operation;
        this.params = params;
    }
    
    public int getExternalInvokeId() {
        return externalInvokeId;
    }

    public int getInternalInvokeId() {
        return internalInvokeId;
    }
    
    public Byte[] getOperation() {
        return operation;
    }

    public Byte[] getParams() {
        return params;
    }
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TcapComponent other = (TcapComponent) obj;
        if (externalInvokeId == null) {
            if (other.externalInvokeId != null) {
                return false;
            }
        } else if (!externalInvokeId.equals(other.externalInvokeId)) {
            return false;
        }
        if (internalInvokeId == null) {
            if (other.internalInvokeId != null) {
                return false;
            }
        } else if (!internalInvokeId.equals(other.internalInvokeId)) {
            return false;
        }
        if (operation == null) {
            if (other.operation != null) {
                return false;
            }
        } else if (!operation.equals(other.operation)) {
            return false;
        }
        if (params == null) {
            if (other.params != null) {
                return false;
            }
        } else if (!params.equals(other.params)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Invoke [externalInvokeId=" + externalInvokeId
                   + ", internalInvokeId=" + internalInvokeId
                   + ", operation=" + EncodingHelper.bytesToHex(operation)
                   + ", params=" + EncodingHelper.bytesToHex(params) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (internalInvokeId == null ? 0 : internalInvokeId.hashCode());
        result = prime * result + (externalInvokeId == null ? 0 : externalInvokeId.hashCode());
        result = prime * result + (operation == null ? 0 : operation.hashCode());
        result = prime * result + (params == null ? 0 : params.hashCode());
        return result;
    }
}
