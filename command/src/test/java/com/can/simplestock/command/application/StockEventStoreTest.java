package com.can.simplestock.command.application;

import com.can.simplestock.command.domain.StockAggregate;
import com.can.simplestock.command.domain.StockEventStoreRepository;
import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;
import com.can.simplestock.cqrsescore.event.EventModel;
import com.can.simplestock.cqrsescore.exceptions.AggregateNotFoundException;
import com.can.simplestock.cqrsescore.exceptions.OptimisticConcurrencyException;
import com.can.simplestock.cqrsescore.messages.BaseEvent;
import com.can.simplestock.cqrsescore.producers.EventProducer;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockEventStoreTest {
    @Mock
    private StockEventStoreRepository stockEventStoreRepository;
    @Mock
    private EventProducer eventProducer;
    @InjectMocks
    private StockEventStore stockEventStore;

    @Test()
    @DisplayName("when aggregate and incoming event version different, OptimisticConcurrencyException should be thrown")
    void should_throw_OptimisticConcurrencyException_when_aggregate_and_incoming_event_version_different() {
        String aggregateIdentifier = UUID.randomUUID().toString();
        var stockOpenedEvent = StockOpenedEvent.builder()
                .productType(ProductType.ELECTRONICS)
                .productCode("E100")
                .productName("Iphone")
                .availableStock(10)
                .createdDate(new Date())
                .version(1)
                .id(aggregateIdentifier)
                .build();

        var stockOpenedEventModel = EventModel.builder()
                .eventData(stockOpenedEvent)
                .eventType(stockOpenedEvent.getClass().getTypeName())
                .aggregateIdentifier(aggregateIdentifier)
                .aggregateType(StockAggregate.class.getTypeName())
                .occurredOn(new Date())
                .version(1)
                .build();

        when(stockEventStoreRepository.findByAggregateIdentifier(aggregateIdentifier))
                .thenReturn(List.of(stockOpenedEventModel));

        assertThatThrownBy(() -> stockEventStore.saveEvents(aggregateIdentifier, List.of(stockOpenedEvent), 2))
                .isInstanceOf(OptimisticConcurrencyException.class);

        verify(stockEventStoreRepository, times(1)).findByAggregateIdentifier(aggregateIdentifier);
    }

    @Test
    @DisplayName("when aggregate and incoming event version same, event should be saved")
    void should_save_event_when_aggregate_and_incoming_event_version_is_same() {
        String aggregateIdentifier = UUID.randomUUID().toString();

        var stockIncreasedEvent = StockIncreasedEvent.builder()
                .version(2)
                .amount(5)
                .build();

        var stockIncreasedEventModel = EventModel.builder()
                .eventData(stockIncreasedEvent)
                .eventType(stockIncreasedEvent.getClass().getTypeName())
                .aggregateIdentifier(aggregateIdentifier)
                .aggregateType(StockAggregate.class.getTypeName())
                .occurredOn(new Date())
                .version(2)
                .build();
        stockIncreasedEvent.setId(aggregateIdentifier);
        stockIncreasedEventModel.setId(aggregateIdentifier);
        when(stockEventStoreRepository.findByAggregateIdentifier(aggregateIdentifier))
                .thenReturn(List.of(stockIncreasedEventModel));

        when(stockEventStoreRepository.save(any(EventModel.class)))
                .thenReturn(stockIncreasedEventModel);

        doAnswer(invocation -> {
            StockIncreasedEvent eventWillProduced = invocation.getArgument(1);
            System.out.println(MessageFormat.format("Event will be produced {0}", eventWillProduced));
            return null;
        }).when(eventProducer).produce(stockIncreasedEvent.getClass().getSimpleName(), stockIncreasedEvent);

        stockEventStore.saveEvents(aggregateIdentifier, List.of(stockIncreasedEvent), 2);

        verify(stockEventStoreRepository, times(1)).findByAggregateIdentifier(aggregateIdentifier);
        verify(eventProducer, times(1)).produce(stockIncreasedEvent.getClass().getSimpleName(), stockIncreasedEvent);
    }

    @Test
    @DisplayName("when called with wrong aggregate id, AggregateNotFoundException should be thrown")
    void should_throw_AggregateNotFoundException_when_called_with_wrong_aggregate_id() {
        String aggregateIdentifier = UUID.randomUUID().toString();
        var stockOpenedEvent = StockOpenedEvent.builder()
                .productType(ProductType.ELECTRONICS)
                .productCode("E100")
                .productName("Iphone")
                .availableStock(10)
                .createdDate(new Date())
                .version(1)
                .id(aggregateIdentifier)
                .build();

        var stockOpenedEventModel = EventModel.builder()
                .eventData(stockOpenedEvent)
                .eventType(stockOpenedEvent.getClass().getTypeName())
                .aggregateIdentifier(aggregateIdentifier)
                .aggregateType(StockAggregate.class.getTypeName())
                .occurredOn(new Date())
                .version(1)
                .build();
        stockOpenedEvent.setId(aggregateIdentifier);
        stockOpenedEventModel.setId(aggregateIdentifier);

        when(stockEventStoreRepository.findByAggregateIdentifier(aggregateIdentifier))
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> stockEventStore.getEvents(aggregateIdentifier))
                .isInstanceOf(AggregateNotFoundException.class);

        verify(stockEventStoreRepository, times(1)).findByAggregateIdentifier(aggregateIdentifier);

    }

    @Test
    @DisplayName("when called with aggregate id, event list of aggregate should returned")
    void should_return_event_list_when_called_with_aggregate_id() {
        String aggregateIdentifier = UUID.randomUUID().toString();
        var stockOpenedEvent = StockOpenedEvent.builder()
                .productType(ProductType.ELECTRONICS)
                .productCode("E100")
                .productName("Iphone")
                .availableStock(10)
                .createdDate(new Date())
                .version(1)
                .id(aggregateIdentifier)
                .build();

        var stockOpenedEventModel = EventModel.builder()
                .eventData(stockOpenedEvent)
                .eventType(stockOpenedEvent.getClass().getTypeName())
                .aggregateIdentifier(aggregateIdentifier)
                .aggregateType(StockAggregate.class.getTypeName())
                .occurredOn(new Date())
                .version(1)
                .build();
        stockOpenedEvent.setId(aggregateIdentifier);
        stockOpenedEventModel.setId(aggregateIdentifier);

        when(stockEventStoreRepository.findByAggregateIdentifier(aggregateIdentifier))
                .thenReturn(List.of(stockOpenedEventModel));

        List<BaseEvent> events = stockEventStore.getEvents(aggregateIdentifier);
        assertThat(events.get(0))
                .isNotNull()
                .isExactlyInstanceOf(StockOpenedEvent.class)
                .isSameAs(stockOpenedEvent);

        verify(stockEventStoreRepository, times(1)).findByAggregateIdentifier(aggregateIdentifier);
    }

    @Test
    @DisplayName("when event_store_is_empty, should throw IllegalStateException")
    void should_throw_IllegalStateException_when_event_store_is_empty() {
        String aggregateIdentifier = UUID.randomUUID().toString();

        when(stockEventStoreRepository.findAll())
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> stockEventStore.getAggregateIds())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("when called, should return all aggregate ids")
    void should_all_aggregate_ids_when_called() {
        String aggregateIdentifier = UUID.randomUUID().toString();
        var stockOpenedEvent = StockOpenedEvent.builder()
                .productType(ProductType.ELECTRONICS)
                .productCode("E100")
                .productName("Iphone")
                .availableStock(10)
                .createdDate(new Date())
                .version(1)
                .id(aggregateIdentifier)
                .build();

        var stockOpenedEventModel = EventModel.builder()
                .eventData(stockOpenedEvent)
                .eventType(stockOpenedEvent.getClass().getTypeName())
                .aggregateIdentifier(aggregateIdentifier)
                .aggregateType(StockAggregate.class.getTypeName())
                .occurredOn(new Date())
                .version(1)
                .build();
        stockOpenedEvent.setId(aggregateIdentifier);
        stockOpenedEventModel.setId(aggregateIdentifier);

        when(stockEventStoreRepository.findAll())
                .thenReturn(List.of(stockOpenedEventModel));

        List<String> aggregateIds = stockEventStore.getAggregateIds();
        assertThat(aggregateIds)
                .isNotNull()
                .contains(aggregateIdentifier, Index.atIndex(0));

        verify(stockEventStoreRepository,times(1)).findAll();
    }
}