package com.can.simplestock.query.application.handler;

import com.can.simplestock.common.events.StockClosedEvent;
import com.can.simplestock.common.events.StockDecreasedEvent;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;
import com.can.simplestock.query.domain.Stock;
import com.can.simplestock.query.domain.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockEventHandler implements EventHandler {

    private final StockRepository repository;

    @Autowired
    public StockEventHandler(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public void on(StockOpenedEvent event) {
        var stock = Stock.builder()
                .id(event.getId())
                .productName(event.getProductName())
                .productCode(event.getProductCode())
                .productType(event.getProductType())
                .availableStock(event.getAvailableStock())
                .createdDate(event.getCreatedDate())
                .build();

        repository.save(stock);
    }

    @Override
    public void on(StockIncreasedEvent event) {
        var stock = repository.findById(event.getId());
        if (stock.isEmpty()) {
            return;
        }

        var availableInStock = stock.get().getAvailableStock();
        var latestStockAmount = availableInStock + event.getAmount();
        stock.get().setAvailableStock(latestStockAmount);

        repository.save(stock.get());
    }

    @Override
    public void on(StockDecreasedEvent event) {
        var stock = repository.findById(event.getId());
        if (stock.isEmpty()) {
            return;
        }

        var availableInStock = stock.get().getAvailableStock();
        var latestStockAmount = availableInStock - event.getAmount();
        stock.get().setAvailableStock(latestStockAmount);

        repository.save(stock.get());
    }

    @Override
    public void on(StockClosedEvent event) {
        repository.deleteById(event.getId());
    }
}
