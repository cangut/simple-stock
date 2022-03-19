package com.can.simplestock.command.domain;

import com.can.simplestock.command.api.commands.CloseStockCommand;
import com.can.simplestock.command.api.commands.DecreaseStockCommand;
import com.can.simplestock.command.api.commands.IncreaseStockCommand;
import com.can.simplestock.command.api.commands.OpenStockCommand;
import com.can.simplestock.common.events.StockClosedEvent;
import com.can.simplestock.common.events.StockDecreasedEvent;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;
import com.can.simplestock.cqrsescore.domain.AggregateRoot;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StockAggregate extends AggregateRoot {

    private String productCode;
    private String productName;
    private int availableInStock;
    private boolean active;

    public StockAggregate(OpenStockCommand command) {
        var stockOpenedEvent = StockOpenedEvent.builder()
                .id(command.getId())
                .productCode(command.getProductCode())
                .productName(command.getProductName())
                .productType(command.getProductType())
                .availableStock(command.getAvailableStock())
                .createdDate(new Date())
                .build();
        raiseEvent(stockOpenedEvent);
    }

    private void apply(StockOpenedEvent event) {
        this.id = event.getId();
        this.productCode = event.getProductCode();
        this.productName = event.getProductName();
        this.availableInStock = event.getAvailableStock();
        this.active = true;
    }

    public void increaseStock(IncreaseStockCommand command) {
        if (!this.active) {
            throw new IllegalStateException("Stock have already been closed.");
        }
        if (command.getAmount() <= 0) {
            throw new IllegalStateException("Amount has to be greater than zero");
        }

        var stockIncreasedEvent = StockIncreasedEvent.builder()
                .id(command.getId())
                .amount(command.getAmount())
                .build();
        raiseEvent(stockIncreasedEvent);
    }

    private void apply(StockIncreasedEvent event) {
        this.id = event.getId();
        this.availableInStock += event.getAmount();
    }

    public void decreaseStock(DecreaseStockCommand command) {
        if (!this.active) {
            throw new IllegalStateException("Stock have already been closed.");
        }
        if (command.getAmount() <= 0) {
            throw new IllegalStateException("Amount has to be greater than zero");
        }

        var stockIncreasedEvent = StockDecreasedEvent.builder()
                .id(command.getId())
                .amount(command.getAmount())
                .build();
        raiseEvent(stockIncreasedEvent);
    }

    private void apply(StockDecreasedEvent event) {
        this.id = event.getId();
        this.availableInStock -= event.getAmount();
    }

    public void closeStock(CloseStockCommand command) {
        if (!this.active) {
            throw new IllegalStateException("Stock have already been closed.");
        }
        var closeStockEvent = StockClosedEvent.builder()
                .id(command.getId())
                .build();
        raiseEvent(closeStockEvent);
    }

    public void apply(StockClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }


}
