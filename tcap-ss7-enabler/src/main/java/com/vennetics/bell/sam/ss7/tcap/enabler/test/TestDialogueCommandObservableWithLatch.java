package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;

public class TestDialogueCommandObservableWithLatch extends HystrixObservableCommand<IResult> {

    private static final Logger logger = LoggerFactory.getLogger(TestDialogueCommandObservableWithLatch.class);

    private ITestDialogue dialogue;
    
    public TestDialogueCommandObservableWithLatch(final ITestDialogue dialogue) {
        super(HystrixCommandGroupKey.Factory.asKey("TestDialogue"));
        this.dialogue = dialogue;
    }

    @Override
    protected Observable<IResult> construct() {
        logger.debug("Handling event on thread: {}", Thread.currentThread().getId());
        logger.debug("Handling event on thread: {}", Thread.currentThread().getId());
        try {
            dialogue.getLatch().await();
        } catch (Exception ex) {
            logger.error("Exception thrown {}", ex);
        }
        return Observable.just(dialogue.getResult());
    }

}
