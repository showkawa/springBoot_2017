package com.kawa.reactor3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Flow;

/**
 * BrianSubscriber
 */
@Slf4j
public class BrianSubscriber implements Flow.Subscriber<Integer> {

    private Flow.Subscription subscription;


    /**
     * when publisher run subscribe() will call this method
     *
     * @param subscription
     */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.info("--- call BrianSubscriber - onSubscribe() ---");

        // first subscribe 10 items
        subscription.request(10L);
    }

    /**
     * subscription.request() will first call onNext()
     *
     * @param item
     */
    @Override
    public void onNext(Integer item) {
        log.info("--- consume item: {}", item);

//        try {
//            TimeUnit.MICROSECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        this.subscription.request(10L);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("--- BrianSubscriber onError ---: {}", throwable.getMessage());
        // cancel the subscription
        subscription.cancel();

    }

    @Override
    public void onComplete() {
        log.info("--- BrianSubscriber onComplete ---");
    }
}
