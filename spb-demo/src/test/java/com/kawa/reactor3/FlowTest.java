package com.kawa.reactor3;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;


@Slf4j
public class FlowTest {

    @Test
    public void Flow_Test_Subscriber() {

        // create publisher (use jdk publisher)
        try (SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
            // create subscriber
            BrianSubscriber subscriber = new BrianSubscriber();
            // create subscription, will call BrianSubscription onSubscribe() method
            publisher.subscribe(subscriber);

            // publisher create the test items
            int count = 0;
            while (++count <= 100) {
                log.info("--- create test item ---: {}", count);
                publisher.submit(count);
            }
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
