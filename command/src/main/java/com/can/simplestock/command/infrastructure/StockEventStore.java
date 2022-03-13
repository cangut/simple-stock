package com.can.simplestock.command.infrastructure;

import com.can.simplestock.command.domain.StockAggregate;
import com.can.simplestock.command.domain.StockEventStoreRepository;
import com.can.simplestock.cqrsescore.event.EventModel;
import com.can.simplestock.cqrsescore.event.EventStore;
import com.can.simplestock.cqrsescore.exceptions.AggregateNotFoundException;
import com.can.simplestock.cqrsescore.exceptions.OptimisticConcurrencyException;
import com.can.simplestock.cqrsescore.messages.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockEventStore implements EventStore {

    private final StockEventStoreRepository eventStoreRepository;

    @Autowired
    public StockEventStore(StockEventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void saveEvents(String aggregateId, List<BaseEvent> events, int expectedVersion) {
        var eventList = eventStoreRepository.findByAggregateIdentifier(aggregateId);

        if (expectedVersion != -1 && eventList.get(eventList.size() - 1).getVersion() != expectedVersion) {
            throw new OptimisticConcurrencyException();
        }

        int version = expectedVersion;

        for (BaseEvent event : events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(StockAggregate.class.getTypeName())
                    .occurredOn(new Date())
                    .eventType(event.getClass().getTypeName())
                    .version(version)
                    .eventData(event)
                    .build();

            var persistentEvent = eventStoreRepository.save(eventModel);
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventList = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventList == null || eventList.isEmpty()) {
            throw new AggregateNotFoundException("Aggregate id is wrong");
        }

        return eventList.stream()
                .map(EventModel::getEventData)
                .collect(Collectors.toList());
    }
}
