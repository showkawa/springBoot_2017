package com.kawa.thread.disruptor;

import com.kawa.thread.disruptor.consumer.LongEventHandler;
import com.kawa.thread.disruptor.domain.LongEvent;
import com.kawa.thread.disruptor.factory.LongEventFactory;
import com.kawa.thread.disruptor.producer.LongEventProducer;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorTest {

    public static void main(String[] args) {
        //1.创建课缓存的线程池，提供给customer
        ExecutorService executorService = Executors.newCachedThreadPool();
        //2.创建工厂
        EventFactory<LongEvent> longEventFactory = new LongEventFactory();
        //3.创建ringBuffer 大小
        int ringBufferSize = 1024*1024;
        //4.创建Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(longEventFactory,ringBufferSize,executorService
        ,ProducerType.MULTI,new YieldingWaitStrategy());
        //5.连接消费端方法
        disruptor.handleEventsWith(new LongEventHandler());
        //6.启动
        disruptor.start();
        //7.创建RingBuffer容器
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        //8.创建生产者
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        //9.指定缓冲大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (int i = 0; i <=1000000 ; i++) {
            byteBuffer.putLong(0,i);
            producer.onData(byteBuffer);
        }
        //10.关闭disruptor和executor
        disruptor.shutdown();
        executorService.shutdown();

    }
}
