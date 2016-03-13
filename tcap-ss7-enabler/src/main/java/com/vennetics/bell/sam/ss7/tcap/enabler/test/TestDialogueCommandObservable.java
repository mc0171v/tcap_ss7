package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;

public class TestDialogueCommandObservable extends HystrixObservableCommand<IResult> {

    private static final Logger logger = LoggerFactory.getLogger(TestDialogueCommandObservable.class);

    private ITestDialogue dialogue;
    
    public TestDialogueCommandObservable(final ITestDialogue dialogue) {
        super(HystrixCommandGroupKey.Factory.asKey("TestDialogue"));
        this.dialogue = dialogue;
    }

    @Override
    protected Observable<IResult> construct() {
        logger.debug("Handling event on thread: {}", Thread.currentThread().getId());
        return Observable.just(dialogue.getResult());
    }

}
