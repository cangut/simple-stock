package com.can.simplestock.command.application;

import com.can.simplestock.command.api.commands.*;
import com.can.simplestock.command.domain.StockAggregate;
import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.cqrsescore.handlers.EventSourcingHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockCommandHandlerTest {

    @Mock
    private EventSourcingHandler<StockAggregate> eventSourcingHandler;

    @InjectMocks
    private StockCommandHandler stockCommandHandler;

    @Test
    void should_save_aggregate_state_when_open_stock_command_comes() {
        OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
        String id = UUID.randomUUID().toString();
        openStockCommand.setId(id);
        StockAggregate stockAggregate = new StockAggregate(openStockCommand);

        doAnswer(invocation -> {
            StockAggregate aggregate = invocation.getArgument(0);
            System.out.println(MessageFormat.format("aggregate state will be saved : {0}", aggregate));
            return null;
        }).when(eventSourcingHandler).save(stockAggregate);

        stockCommandHandler.handle(openStockCommand);

        verify(eventSourcingHandler, times(1)).save(stockAggregate);
    }

    @Test
    void should_save_aggregate_state_when_increase_stock_command_comes() {

        OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
        String id = UUID.randomUUID().toString();
        openStockCommand.setId(id);
        StockAggregate stockAggregate = new StockAggregate(openStockCommand);

        IncreaseStockCommand increaseStockCommand = new IncreaseStockCommand(5);
        increaseStockCommand.setId(id);

        when(eventSourcingHandler.getById(openStockCommand.getId())).thenReturn(stockAggregate);

        doAnswer(invocation -> {
            StockAggregate aggregate = invocation.getArgument(0);
            System.out.println(MessageFormat.format("aggregate state will be saved : {0}", aggregate));
            return null;
        }).when(eventSourcingHandler).save(stockAggregate);

        stockCommandHandler.handle(increaseStockCommand);

        assertThat(stockAggregate.getAvailableInStock())
                .isEqualTo(15);

        verify(eventSourcingHandler, times(1)).getById(id);
        verify(eventSourcingHandler, times(1)).save(stockAggregate);
    }

    @Test
    void should_save_aggregate_state_when_decrease_stock_command_comes() {

        OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
        String id = UUID.randomUUID().toString();
        openStockCommand.setId(id);
        StockAggregate stockAggregate = new StockAggregate(openStockCommand);

        DecreaseStockCommand decreaseStockCommand = new DecreaseStockCommand(5);
        decreaseStockCommand.setId(id);

        when(eventSourcingHandler.getById(openStockCommand.getId())).thenReturn(stockAggregate);

        doAnswer(invocation -> {
            StockAggregate aggregate = invocation.getArgument(0);
            System.out.println(MessageFormat.format("aggregate state will be saved : {0}", aggregate));
            return null;
        }).when(eventSourcingHandler).save(stockAggregate);

        stockCommandHandler.handle(decreaseStockCommand);

        assertThat(stockAggregate.getAvailableInStock())
                .isEqualTo(5);

        verify(eventSourcingHandler, times(1)).getById(id);
        verify(eventSourcingHandler, times(1)).save(stockAggregate);
    }

    @Test
    void should_save_aggregate_state_when_close_stock_command_comes() {

        OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
        String id = UUID.randomUUID().toString();
        openStockCommand.setId(id);
        StockAggregate stockAggregate = new StockAggregate(openStockCommand);

        CloseStockCommand closeStockCommand = new CloseStockCommand(id);
        closeStockCommand.setId(id);

        when(eventSourcingHandler.getById(openStockCommand.getId())).thenReturn(stockAggregate);

        doAnswer(invocation -> {
            StockAggregate aggregate = invocation.getArgument(0);
            System.out.println(MessageFormat.format("aggregate state will be saved : {0}", aggregate));
            return null;
        }).when(eventSourcingHandler).save(stockAggregate);

        stockCommandHandler.handle(closeStockCommand);

        assertFalse(stockAggregate.isActive());

        verify(eventSourcingHandler, times(1)).getById(id);
        verify(eventSourcingHandler, times(1)).save(stockAggregate);
    }

    @Test
    void should_restore_when_restore_command_comes() {

        RestoreCommand restoreCommand = new RestoreCommand();

        stockCommandHandler.handle(restoreCommand);

        verify(eventSourcingHandler, times(1)).republishEvents();
    }
}