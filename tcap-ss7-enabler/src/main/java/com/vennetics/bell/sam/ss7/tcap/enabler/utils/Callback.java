package com.vennetics.bell.sam.ss7.tcap.enabler.utils;

import com.vennetics.bell.sam.ss7.tcap.enabler.rest.OutboundATIMessage;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Callback {

    private PublishSubject<OutboundATIMessage> subject;
    
    private IResultListener listener;
    
    public Callback(final PublishSubject<OutboundATIMessage> subject, final IResultListener listener) {
        super();
        this.subject = subject;
        this.listener = listener;
    }
    
    public Callback() {
        super();
        this.subject = PublishSubject.create();
        this.listener = new ResultListener(this.subject);
    }

    public PublishSubject<OutboundATIMessage> getSubject() {
        return subject;
    }

    public void setSubject(final PublishSubject<OutboundATIMessage> subject) {
        this.subject = subject;
    }

    public IResultListener getListener() {
        return listener;
    }

    public void setListener(final IResultListener listener) {
        this.listener = listener;
    }
    
    public Observable<OutboundATIMessage> getObservable() {
        return subject;
    }

}
