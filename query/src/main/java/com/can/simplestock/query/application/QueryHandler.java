package com.can.simplestock.query.application;

import com.can.simplestock.query.api.queries.FindAllStock;
import com.can.simplestock.query.api.queries.FindStockByAmount;
import com.can.simplestock.query.api.queries.FindStockById;
import com.can.simplestock.query.api.queries.FindStockByProductCode;
import com.can.simplestock.query.domain.AbstractStockEntity;
import com.can.simplestock.query.domain.Stock;

import java.util.List;

public interface QueryHandler {
    List<Stock> handle(FindAllStock query);
    List<Stock> handle(FindStockById query);
    List<Stock> handle(FindStockByAmount query);
    List<Stock> handle(FindStockByProductCode query);
}
