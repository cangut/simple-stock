package com.can.simplestock.cqrsescore.query;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> classType, QueryHandlerMethod<T> handlerMethod);
    void send(BaseQuery query);
}
