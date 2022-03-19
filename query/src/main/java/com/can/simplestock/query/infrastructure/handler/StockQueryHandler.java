package com.can.simplestock.query.infrastructure.handler;

import com.can.simplestock.common.constants.EqualityType;
import com.can.simplestock.query.api.queries.FindAllStock;
import com.can.simplestock.query.api.queries.FindStockByAmount;
import com.can.simplestock.query.api.queries.FindStockById;
import com.can.simplestock.query.api.queries.FindStockByProductCode;

import com.can.simplestock.query.domain.Stock;
import com.can.simplestock.query.domain.StockRepository;
import com.can.simplestock.query.infrastructure.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class StockQueryHandler implements QueryHandler {

    private final StockRepository repository;

    @Autowired
    public StockQueryHandler(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Stock> handle(FindAllStock query) {
        Iterable<Stock> stocks = repository.findAll();
        List<Stock> stockList = new ArrayList<>();
        stocks.forEach(stockList::add);
        return stockList;
    }

    @Override
    public List<Stock> handle(FindStockById query) {
        var stock = repository.findById(query.getId());
        if(stock.isEmpty()) return null;
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock.get());
        return stockList;
    }

    @Override
    public List<Stock> handle(FindStockByAmount query) {
        List<Stock> stockList =
                query.getEqualityType() == EqualityType.GREATER_THAN ?
                        repository.findByAvailableStockGreaterThan(query.getAmount()) :
                        repository.findByAvailableStockLessThan(query.getAmount());
        return stockList;
    }

    @Override
    public List<Stock> handle(FindStockByProductCode query) {
        var stock = repository.findByProductCode(query.getProductCode());
        if(stock.isEmpty()) return null;
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock.get());
        return stockList;
    }
}
