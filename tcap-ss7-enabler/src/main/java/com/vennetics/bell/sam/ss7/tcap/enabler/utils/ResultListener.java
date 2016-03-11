package com.vennetics.bell.sam.ss7.tcap.enabler.utils;

import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import rx.subjects.PublishSubject;

public class ResultListener implements IResultListener {

    private PublishSubject<OutboundATIMessage> subject;

    public ResultListener(final PublishSubject<OutboundATIMessage> subject) {
        super();
        this.subject = subject;
    }
    
    public void handleEvent(final OutboundATIMessage message) {
        subject.onNext(message);
    }
    
    public void setSubject(final PublishSubject<OutboundATIMessage> subject) {
        this.subject = subject;
    }

    public PublishSubject<OutboundATIMessage> getSubject() {
        return subject;
    }
}
