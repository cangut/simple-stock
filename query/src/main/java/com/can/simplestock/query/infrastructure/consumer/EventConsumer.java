package com.can.simplestock.query.infrastructure.consumer;

import com.can.simplestock.common.events.StockClosedEvent;
import com.can.simplestock.common.events.StockDecreasedEvent;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload StockOpenedEvent event, Acknowledgment ack);
    void consume(@Payload StockIncreasedEvent event, Acknowledgment ack);
    void consume(@Payload StockDecreasedEvent event, Acknowledgment ack);
    void consume(@Payload StockClosedEvent event, Acknowledgment ack);
}
