package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class TestDialogueCommand extends HystrixCommand<IResult> {

    private static final Logger logger = LoggerFactory.getLogger(TestDialogueCommand.class);

    private ITestDialogue dialogue;
    
    public TestDialogueCommand(final ITestDialogue dialogue) {
        super(HystrixCommandGroupKey.Factory.asKey("TestDialogue"));
        this.dialogue = dialogue;
    }

    @Override
    protected IResult run() {
        logger.debug("Handling event on thread: {}", Thread.currentThread().getId());
        return dialogue.getResult();
    }

}
