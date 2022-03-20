package com.can.simplestock.command.infrastructure.command;

import com.can.simplestock.command.api.commands.*;
import com.can.simplestock.command.domain.StockAggregate;
import com.can.simplestock.cqrsescore.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockCommandHandler implements CommandHandler {

    private final EventSourcingHandler<StockAggregate> eventSourcingHandler;

    @Autowired
    public StockCommandHandler(EventSourcingHandler<StockAggregate> eventSourcingHandler) {
        this.eventSourcingHandler = eventSourcingHandler;
    }

    @Override
    public void handle(OpenStockCommand command) {
        var aggregate = new StockAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(IncreaseStockCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.increaseStock(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DecreaseStockCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        if (command.getAmount() > aggregate.getAvailableInStock()) {
            throw new IllegalStateException("Decrease amount is greater than available stock. Available Stock: " + aggregate.getAvailableInStock());
        }
        aggregate.decreaseStock(command);
        eventSourcingHandler.save(aggregate);

    }

    @Override
    public void handle(CloseStockCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeStock(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(RestoreCommand command) {
        eventSourcingHandler.republishEvents();
    }
}
