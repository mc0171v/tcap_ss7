package com.vennetics.bell.sam.ss7.tcap.common.dialogue;

public enum DialoguePrimitive {
    BEGIN,
    CONTINUE,
    END;
    
    public String value() {
        return name();
    }
}
