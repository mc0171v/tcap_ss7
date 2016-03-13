package com.vennetics.bell.sam.ss7.tcap.enabler.test;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Subscriber;

@RunWith(MockitoJUnitRunner.class)
public class TestDialogueTest {

    private static final Logger logger = LoggerFactory.getLogger(TestDialogueTest.class);

    @Test
    public void dialogueTest() {
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       final IResult result = new Result("Result 1");
       final IEvent event = new Event(result);
       dialogue.handleEventWOLatch(event);
       logger.debug("Result is " + dialogue.getResult().toString());
    }
    
    @Test
    public void dialogueTestReturnResult() {
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       final Thread t1 = new DialogueThreadWOLatch(dialogue);
       t1.start();
       if (dialogue.getResult() != null) {
           logger.debug("Result is " + dialogue.getResult().toString());
       } else {
           logger.debug("Result is not set");
       }
    }
    
    @Test
    public void dialogueTestReturnResultCDL() {
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       CountDownLatch cDl = new CountDownLatch(1);
       dialogue.setLatch(cDl);
       final Thread t1 = new DialogueThread(dialogue);
       t1.start();
        try {
            cDl.await();
            if (dialogue.getResult() != null) {
                logger.debug("Result is " + dialogue.getResult().toString());
            } else {
                logger.debug("Result is not set");
            }
        } catch (InterruptedException ex) {
            logger.error("Interrupted");
        }
    }
    
    @Test
    public void dialogueTestReturnResultHysterixNoLatch() {
       logger.debug("Executing test on thread: {}", Thread.currentThread().getId());
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       IResult result = (new TestDialogueCommand(dialogue)).execute();
       final Thread t1 = new DialogueThreadWOLatch(dialogue);
       t1.start();
       if (result != null) {
           logger.debug("Result is " + dialogue.getResult().toString());
       } else {
           logger.debug("Result is not set");
       }
    }
    
    @Test
    public void dialogueTestReturnResultHysterixLatch() {
       logger.debug("Executing test on thread: {}", Thread.currentThread().getId());
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       CountDownLatch cDl = new CountDownLatch(1);
       dialogue.setLatch(cDl);
       final Thread t1 = new DialogueThread(dialogue);
       t1.start();
       //Blocks
       IResult result = (new TestDialogueCommandWithLatch(dialogue)).execute();
       if (result != null) {
           logger.debug("Result is " + dialogue.getResult().toString());
       } else {
           logger.debug("Result is not set");
       }
    }
    
    @Test
    public void dialogueTestReturnResultHysterixLatchObservable() {
       logger.debug("Executing test on thread: {}", Thread.currentThread().getId());
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       CountDownLatch cDl = new CountDownLatch(1);
       dialogue.setLatch(cDl);
       final ResultWrapper resultWrapper = new ResultWrapper();
       logger.debug("Subscribing");
       //Doesn't block
       Observable<IResult> observable = (new TestDialogueCommandWithLatch(dialogue)).observe();
       observable.subscribe(item -> { resultWrapper.setResult(item); });
       logger.debug("Subscribed");
       final Thread t1 = new DialogueThread(dialogue);
       t1.start();
       if (resultWrapper.getResult() != null) {
           logger.debug("Result is " + dialogue.getResult().toString());
       } else {
           logger.debug("Result is not set");
       }
    }
    
    @Test
    public void dialogueTestReturnResultHysterixObservable() {
       logger.debug("Executing test on thread: {}", Thread.currentThread().getId());
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);
       final ResultWrapper resultWrapper = new ResultWrapper();
       logger.debug("Subscribing");
       //Doesn't block
       Observable<IResult> observable = (new TestDialogueCommandObservable(dialogue)).observe();
       logger.debug("Subscribed");
       final Thread t1 = new DialogueThreadWOLatch(dialogue);
       t1.start();
       final Subscriber<IResult> subscriber = new Subscriber<IResult>() {
           @Override
           public void onCompleted() {
               
           }
           @Override
           public void onError(final Throwable throwable) {
               logger.debug("Throw in subscriber");
           }
           @Override
           public void onNext(final IResult result) {
               logger.debug("Setting result in subscriber");
               resultWrapper.setResult(result);
               onCompleted();
           }
       };
       observable.subscribe(subscriber);
       if (resultWrapper.getResult() != null) {
           logger.debug("Result is " + dialogue.getResult().toString());
       } else {
           logger.debug("Result is not set");
       }
       try {
           System.in.read();
       } catch (Exception ex) {
           logger.error("read exception {}", ex);
       }
    }
    
    @Test
    public void dialogueTestReturnResultHysterixObservableWithLatch() {
       logger.debug("Executing test on thread: {}", Thread.currentThread().getId());
       final ITestDialogue dialogue =  new TestDialogue();
       final ITestState state = new TestStartState(dialogue);
       dialogue.setState(state);       CountDownLatch cDl = new CountDownLatch(1);
       dialogue.setLatch(cDl);
       final ResultWrapper resultWrapper = new ResultWrapper();
       logger.debug("Subscribing");
       final Thread t1 = new DialogueThread(dialogue);
       t1.start();
       //Blocks
       Observable<IResult> observable = (new TestDialogueCommandObservableWithLatch(dialogue)).observe();
       logger.debug("Subscribed");
       final Subscriber<IResult> subscriber = new Subscriber<IResult>() {
           @Override
           public void onCompleted() {
               
           }
           @Override
           public void onError(final Throwable throwable) {
               logger.debug("Throw in subscriber");
           }
           @Override
           public void onNext(final IResult result) {
               logger.debug("Setting result in subscriber");
               resultWrapper.setResult(result);
               onCompleted();
           }
       };
       observable.subscribe(subscriber);
       if (resultWrapper.getResult() != null) {
           logger.debug("Result is " + dialogue.getResult().toString());
       } else {
           logger.debug("Result is not set");
       }
       try {
           System.in.read();
       } catch (Exception ex) {
           logger.error("read exception {}", ex);
       }
    }
    
    public class DialogueThread extends Thread {
        
        private ITestDialogue dialogue;
        
        DialogueThread(final ITestDialogue dialogue) {
            this.dialogue = dialogue;
        }

        public void run() {
            try {
                logger.debug("Waiting 3000 before sending event");
                Thread.sleep(3000);
                final IResult result = new Result("Result 1");
                final IEvent event = new Event(result);
                logger.debug("Sending event");
                dialogue.handleEvent(event);
            } catch (Exception ex) {
                logger.debug("Exception caught {}", ex);
            }
        }

    }
    
    public class DialogueThreadWOLatch extends Thread {
        
        private ITestDialogue dialogue;
        
        DialogueThreadWOLatch(final ITestDialogue dialogue) {
            this.dialogue = dialogue;
        }

        public void run() {
            try {
                Thread.sleep(3000);
                final IResult result = new Result("Result 1");
                final IEvent event = new Event(result);
                dialogue.handleEventWOLatch(event);
            } catch (Exception ex) {
                logger.debug("Exception caught {}", ex);
            }
        }

    }
    
}
