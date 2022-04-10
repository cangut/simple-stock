package com.can.simplestock.query;

import com.can.simplestock.cqrsescore.query.QueryDispatcher;
import com.can.simplestock.query.api.queries.FindAllStock;
import com.can.simplestock.query.api.queries.FindStockByAmount;
import com.can.simplestock.query.api.queries.FindStockById;
import com.can.simplestock.query.api.queries.FindStockByProductCode;
import com.can.simplestock.query.application.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

	private final QueryDispatcher dispatcher;
	private final QueryHandler handler;

	@Autowired
	public QueryApplication(QueryDispatcher dispatcher, QueryHandler handler) {
		this.dispatcher = dispatcher;
		this.handler = handler;
	}

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers(){
		dispatcher.registerHandler(FindAllStock.class, handler::handle);
		dispatcher.registerHandler(FindStockById.class, handler::handle);
		dispatcher.registerHandler(FindStockByProductCode.class, handler::handle);
		dispatcher.registerHandler(FindStockByAmount.class, handler::handle);
	}

}
