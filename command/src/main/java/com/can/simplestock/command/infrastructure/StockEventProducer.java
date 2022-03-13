package com.can.simplestock.command.infrastructure;

import com.can.simplestock.cqrsescore.messages.BaseEvent;
import com.can.simplestock.cqrsescore.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public StockEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(String topic, BaseEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
