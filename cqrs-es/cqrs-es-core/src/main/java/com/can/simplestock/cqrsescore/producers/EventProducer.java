package com.can.simplestock.cqrsescore.producers;

import com.can.simplestock.cqrsescore.messages.BaseEvent;

public interface EventProducer {
    void produce(String topic, BaseEvent event);
}
