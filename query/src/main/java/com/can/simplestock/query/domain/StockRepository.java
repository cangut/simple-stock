package com.can.simplestock.query.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, String> {
    Optional<Stock> findByProductCode(String productCode);

    List<Stock> findByAvailableStockGreaterThan(int amount);

    List<Stock> findByAvailableStockLessThan(int amount);
}
