package com.kawa.thread.disruptor.consumer;

import com.kawa.thread.disruptor.domain.LongEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Event消费者
 *
 *  消费者获取生成者推送的数据
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        System.out.println(Thread.currentThread().getName()+ " event: " + longEvent.getValue());
    }
}
