package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class TestDialogueCommandWithLatch extends HystrixCommand<IResult> {

    private static final Logger logger = LoggerFactory.getLogger(TestDialogueCommandWithLatch.class);

    private ITestDialogue dialogue;
    
    public TestDialogueCommandWithLatch(final ITestDialogue dialogue) {
        super(HystrixCommandGroupKey.Factory.asKey("TestDialogue"), 10000);
        this.dialogue = dialogue;
    }

    @Override
    protected IResult run() {
        logger.debug("Handling event on thread: {}", Thread.currentThread().getId());
        try {
            dialogue.getLatch().await();
        } catch (Exception ex) {
            logger.error("Exception thrown {}", ex);
        }

        return dialogue.getResult();
    }

}
