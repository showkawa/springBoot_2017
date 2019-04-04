package com.kawa.thread.disruptor.producer;

import com.kawa.thread.disruptor.domain.LongEvent;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducer {

    public final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer byteBuffer) {
        //1.ringBuffer 事件队列的下一个槽
        long sequence = ringBuffer.next();

        Long data = null;

        try {
            //2.取出空的事件队列
            LongEvent longEvent = ringBuffer.get(sequence);
            data = byteBuffer.getLong(0);
            //3.获取事件队列传递的数据
            longEvent.setValue(data);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //4.发布事件
            ringBuffer.publish(sequence);
        }


    }
}
