package com.kawa.reactor3;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/**
 * 反应式流从2013年开始，作为提供非阻塞回压的异步流处理标准的倡议, 旨在处理元素流（即消息流数据流）的问题 -- 如何将元素从发布者传递到订阅者，而不需要发布者阻塞，不需要订阅者有无边界缓冲区，不需要订阅者丢弃无法处理的元素。
 * 反应式流模型可以解决这个问题，该模型非常简单：订阅者向发布者发送异步请求，订阅n个元素；然后发布者向订阅者异步发送n个或少于n个元素。
 * 反应式流会在pull模型和push模型流处理机制之间动态切换。当发布者快，订阅者慢，它使用pull模型；当发布者慢，订阅者快，它使用push模型。即谁慢谁占主动
 */
@Slf4j
public class FlowTest {

    /**
     * Publisher to Subscriber (PS)
     */
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
            while (++count <= 1000) {
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

    /**
     * Publisher to Processor to Subscriber (PPS)
     */
    @Test
    public void Flow_Test_Processor() {

        // create publisher (use jdk publisher)
        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            // create processor
            BrianProcessor processor = new BrianProcessor();
            // create subscriber
            BrianSubscriber subscriber = new BrianSubscriber();
            // create subscription, will call BrianSubscription onSubscribe() method
            publisher.subscribe(processor);
            processor.subscribe(subscriber);

            // publisher create the test items
            int count = 0;
            while (++count <= 1000) {
                log.info("--- create test item ---: {}", count);
                publisher.submit(String.valueOf(count) + "%3D");
            }
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
