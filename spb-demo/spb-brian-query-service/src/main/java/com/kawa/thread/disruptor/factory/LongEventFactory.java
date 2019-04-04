package com.kawa.thread.disruptor.factory;

import com.kawa.thread.disruptor.domain.LongEvent;
import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
