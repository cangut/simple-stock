package com.can.simplestock.command.application;

import com.can.simplestock.command.api.commands.OpenStockCommand;
import com.can.simplestock.command.domain.StockAggregate;
import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.common.events.StockOpenedEvent;
import com.can.simplestock.cqrsescore.event.EventStore;
import com.can.simplestock.cqrsescore.producers.EventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class StockEventSourcingHandlerTest {

    @Mock
    private EventStore eventStore;

    @Mock
    private EventProducer eventProducer;

    @InjectMocks
    private StockEventSourcingHandler stockEventSourcingHandler;

    @Test
    void should_save_aggregate_state_when_aggregate_is_given() {
        OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
        String id = UUID.randomUUID().toString();
        openStockCommand.setId(id);
        StockAggregate stockAggregate = new StockAggregate(openStockCommand);

        var event = stockAggregate.getUnconsumedDomainEvents().get(0);

        assertThat(event)
                .isNotNull()
                .isExactlyInstanceOf(StockOpenedEvent.class);

        stockEventSourcingHandler.save(stockAggregate);

        assertThat(stockAggregate.getUnconsumedDomainEvents())
                .isEmpty();

        verify(eventStore, times(1)).saveEvents(stockAggregate.getId(), stockAggregate.getUnconsumedDomainEvents(), stockAggregate.getVersion());
    }

    @Test
    void should_return_aggregate_when_id_is_supplied() {
        String id = UUID.randomUUID().toString();
        var stockOpenedEvent = StockOpenedEvent.builder()
                .productType(ProductType.ELECTRONICS)
                .productCode("E100")
                .productName("Iphone")
                .availableStock(10)
                .createdDate(new Date())
                .version(1)
                .id(id)
                .build();
        when(eventStore.getEvents(id)).thenReturn(List.of(stockOpenedEvent));
        StockAggregate aggregate = stockEventSourcingHandler.getById(id);
        assertThat(aggregate)
                .isNotNull();
    }

    @Test
    void should_produce_events_when_republishEvents_called(){
        OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
        String id = UUID.randomUUID().toString();
        openStockCommand.setId(id);
        StockAggregate stockAggregate = new StockAggregate(openStockCommand);

        var stockOpenedEvent = StockOpenedEvent.builder()
                .productType(ProductType.ELECTRONICS)
                .productCode("E100")
                .productName("Iphone")
                .availableStock(10)
                .createdDate(new Date())
                .version(1)
                .id(id)
                .build();

        when(eventStore.getAggregateIds()).thenReturn(List.of(id));
        when(eventStore.getEvents(id)).thenReturn(List.of(stockOpenedEvent));

        doAnswer(invocation -> {
           StockOpenedEvent _stockOpenedEvent = invocation.getArgument(1);
           System.out.println(MessageFormat.format("Event will produced: {0}", _stockOpenedEvent));
           return null;
        }).when(eventProducer).produce(stockOpenedEvent.getClass().getSimpleName(), stockOpenedEvent);

        stockEventSourcingHandler.republishEvents();

        verify(eventStore, times(1)).getAggregateIds();
        verify(eventProducer, times(1)).produce(stockOpenedEvent.getClass().getSimpleName(), stockOpenedEvent);

    }
}