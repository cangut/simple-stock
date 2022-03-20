package com.can.simplestock.command.infrastructure.event;

import com.can.simplestock.command.domain.StockAggregate;
import com.can.simplestock.cqrsescore.domain.AggregateRoot;
import com.can.simplestock.cqrsescore.event.EventStore;
import com.can.simplestock.cqrsescore.handlers.EventSourcingHandler;
import com.can.simplestock.cqrsescore.messages.BaseEvent;
import com.can.simplestock.cqrsescore.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class StockEventSourcingHandler implements EventSourcingHandler<StockAggregate> {

    private final EventStore eventStore;
    private final EventProducer eventProducer;

    @Autowired
    public StockEventSourcingHandler(EventStore eventStore, EventProducer eventProducer) {
        this.eventStore = eventStore;
        this.eventProducer = eventProducer;
    }

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUnconsumedDomainEvents(), aggregate.getVersion());
        aggregate.setDomainEventsAsConsumed();
    }

    @Override
    public StockAggregate getById(String aggregateId) {
        var aggregate = new StockAggregate();
        var events = eventStore.getEvents(aggregateId);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var latestVersion = events.stream()
                    .map(BaseEvent::getVersion)
                    .max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }

        return aggregate;
    }

    @Override
    public void republishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for (String aggregateId : aggregateIds) {
            var aggregate = getById(aggregateId);
            if (aggregate == null || !aggregate.isActive()) continue;
            var events = eventStore.getEvents(aggregate.getId());
            for (BaseEvent event : events) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }
}
