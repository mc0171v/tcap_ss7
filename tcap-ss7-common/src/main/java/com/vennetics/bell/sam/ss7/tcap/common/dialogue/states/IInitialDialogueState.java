package com.vennetics.bell.sam.ss7.tcap.common.dialogue.states;

public interface IInitialDialogueState extends IDialogueState {
    
    /***
     * Create a new instance of a dialogue start state {@link IInitialDialogueState}
     * @return {@link IInitialDialogueState}
     *     a new instance of a start dialogue state
     */
    IInitialDialogueState newInstance();
}
