package com.can.simplestock.query.application.consumer;

import com.can.simplestock.common.events.StockClosedEvent;
import com.can.simplestock.common.events.StockDecreasedEvent;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;
import com.can.simplestock.query.application.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class StockEventConsumer implements EventConsumer {

    private final EventHandler eventHandler;

    @Autowired
    public StockEventConsumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    @KafkaListener(topics = "StockOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(StockOpenedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @Override
    @KafkaListener(topics = "StockIncreasedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(StockIncreasedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @Override
    @KafkaListener(topics = "StockDecreasedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(StockDecreasedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @Override
    @KafkaListener(topics = "StockClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(StockClosedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }
}
