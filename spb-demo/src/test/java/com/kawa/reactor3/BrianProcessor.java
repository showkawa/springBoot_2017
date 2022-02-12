package com.kawa.reactor3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@Slf4j
public class BrianProcessor extends SubmissionPublisher<Integer> implements Flow.Subscriber<String> {


    private Flow.Subscription subscription;

    /**
     * when publisher run subscribe() will call this method
     *
     * @param subscription
     */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.info("--- call BrianProcessor - onSubscribe() ---");

        // first subscribe 10 items
        subscription.request(10L);
    }

    /**
     * subscription.request() will first call onNext()
     *
     * @param item
     */
    @Override
    public void onNext(String item) {
        log.info("--- BrianProcessor consume item: {}", item);
        String newIntStr = item.replace("%3D", "");
        log.info("--- BrianProcessor submit item: {}", newIntStr);
        Integer newVal = Integer.valueOf(newIntStr);
        this.submit(newVal);
        // when consume 1 item then request next 10 items
        this.subscription.request(10L);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("--- BrianProcessor onError ---: {}", throwable);
        // cancel the subscription
        subscription.cancel();
    }

    @Override
    public void onComplete() {
        log.info("--- BrianProcessor onComplete ---");
    }
}
