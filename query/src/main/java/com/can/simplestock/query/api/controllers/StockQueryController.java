package com.can.simplestock.query.api.controllers;

import com.can.simplestock.common.constants.EqualityType;
import com.can.simplestock.cqrsescore.query.QueryDispatcher;
import com.can.simplestock.query.api.queries.FindAllStock;
import com.can.simplestock.query.api.queries.FindStockByAmount;
import com.can.simplestock.query.api.queries.FindStockById;
import com.can.simplestock.query.api.queries.FindStockByProductCode;
import com.can.simplestock.query.api.responses.QueryResponse;
import com.can.simplestock.query.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api/v1/stock-query")
public class StockQueryController {

    private final Logger logger = Logger.getLogger(StockQueryController.class.getName());
    private final QueryDispatcher queryDispatcher;

    @Autowired
    public StockQueryController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    @GetMapping(path = "/")
    public ResponseEntity<QueryResponse> getAllStock() {
        try {
            List<Stock> stockList = queryDispatcher.send(new FindAllStock());
            if (isEmpty(stockList)) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = QueryResponse.builder()
                    .stockList(stockList)
                    .message("Stocks retrieved successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.", e);
            return new ResponseEntity<>(new QueryResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/by-id/{id}")
    public ResponseEntity<QueryResponse> getStockById(@PathVariable String id) {
        try {
            List<Stock> stockList = queryDispatcher.send(new FindStockById(id));
            if (isEmpty(stockList)) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = QueryResponse.builder()
                    .stockList(stockList)
                    .message("Stocks retrieved successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.", e);
            return new ResponseEntity<>(new QueryResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/by-product-code/{code}")
    public ResponseEntity<QueryResponse> getStockByProductCode(@PathVariable String code) {
        try {
            List<Stock> stockList = queryDispatcher.send(new FindStockByProductCode(code));
            if (isEmpty(stockList)) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = QueryResponse.builder()
                    .stockList(stockList)
                    .message("Stocks retrieved successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.", e);
            return new ResponseEntity<>(new QueryResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/by-amount/{equalityType}/{amount}")
    public ResponseEntity<QueryResponse> getStockByAmount(@PathVariable int amount, @PathVariable EqualityType equalityType) {
        try {
            List<Stock> stockList = queryDispatcher.send(new FindStockByAmount(equalityType, amount));
            if (isEmpty(stockList)) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = QueryResponse.builder()
                    .stockList(stockList)
                    .message("Stocks retrieved successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.", e);
            return new ResponseEntity<>(new QueryResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isEmpty(List<Stock> list){
        return list == null || list.size() == 0;
    }
}
