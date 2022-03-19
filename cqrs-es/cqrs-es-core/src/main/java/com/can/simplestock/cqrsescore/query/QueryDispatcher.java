package com.can.simplestock.cqrsescore.query;

import com.can.simplestock.cqrsescore.domain.BaseEntity;

import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> classType, QueryHandlerMethod<T> handlerMethod);
    <T extends BaseEntity> List<T> send(BaseQuery query);
}
