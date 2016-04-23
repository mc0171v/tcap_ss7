package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TcapDialogue {

    private final Integer externalDialogueId;
    private final  Integer internalDialogueId;
    private final DialoguePrimitive dialoguePrimitive;
    
    private List<TcapComponent> components;
    
    @JsonCreator
    TcapDialogue(@JsonProperty("externalDialogueId") final Integer externalDialogueId,
                 @JsonProperty("internalDialogueId") final Integer internalDialogueId,
                 @JsonProperty("components") final List<TcapComponent> components,
                 @JsonProperty("dialoguePrimitive") final DialoguePrimitive dialoguePrimitive) {
        this.externalDialogueId = externalDialogueId;
        this.internalDialogueId = internalDialogueId;
        this.components = components;
        this.dialoguePrimitive = dialoguePrimitive;
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
        final TcapDialogue other = (TcapDialogue) obj;
        if (externalDialogueId == null) {
            if (other.externalDialogueId != null) {
                return false;
            }
        } else if (!externalDialogueId.equals(other.externalDialogueId)) {
            return false;
        }
        if (internalDialogueId == null) {
            if (other.internalDialogueId != null) {
                return false;
            }
        } else if (!internalDialogueId.equals(other.internalDialogueId)) {
            return false;
        }
        if (components == null) {
            if (other.components != null) {
                return false;
            }
        } else if (!components.equals(other.components)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Dialogue [externalDialogueId=" + externalDialogueId + ", internalDialogueId=" + internalDialogueId + ","
                        + " components=" + components + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (internalDialogueId == null ? 0 : internalDialogueId.hashCode());
        result = prime * result + (externalDialogueId == null ? 0 : externalDialogueId.hashCode());
        result = prime * result + (components == null ? 0 : components.hashCode());
        return result;
    }

    public DialoguePrimitive getDialoguePrimitive() {
        return dialoguePrimitive;
    }
    
    public Integer getExternalDialogueId() {
        return externalDialogueId;
    }

    public Integer getInternalDialogueId() {
        return internalDialogueId;
    }

    public List<TcapComponent> getComponents() {
        return components;
    }

    public void setComponents(final List<TcapComponent> components) {
        this.components = components;
    }
}
