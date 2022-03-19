package com.can.simplestock.query.infrastructure;

import com.can.simplestock.cqrsescore.domain.BaseEntity;
import com.can.simplestock.cqrsescore.query.BaseQuery;
import com.can.simplestock.cqrsescore.query.QueryDispatcher;
import com.can.simplestock.cqrsescore.query.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> handlers = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> classType, QueryHandlerMethod<T> handlerMethod) {
        var handler = handlers.computeIfAbsent(classType, c -> new ArrayList<>());
        handler.add(handlerMethod);
    }

    @Override
    public <T extends BaseEntity> List<T> send(BaseQuery query) {
        var handler  = handlers.get(query.getClass());

        if (handler == null || handlers.size() == 0) {
            throw new RuntimeException("No handler defined for this query");
        }

        if (handler.size() > 1) {
            throw new RuntimeException("Query has to have only one handler");
        }

        return handler.get(0).handle(query);
    }
}
