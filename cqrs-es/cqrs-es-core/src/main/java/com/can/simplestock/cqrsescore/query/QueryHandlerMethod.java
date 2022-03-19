package com.can.simplestock.cqrsescore.query;

import com.can.simplestock.cqrsescore.domain.BaseEntity;

import java.util.List;

@FunctionalInterface
public interface QueryHandlerMethod<T extends BaseQuery> {
    List<? extends BaseEntity> handle(T query);
}
