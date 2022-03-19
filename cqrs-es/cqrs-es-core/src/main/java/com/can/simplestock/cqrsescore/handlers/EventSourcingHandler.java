package com.can.simplestock.cqrsescore.handlers;

import com.can.simplestock.cqrsescore.domain.AggregateRoot;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregateRoot);
    T getById(String aggregateId);
}
