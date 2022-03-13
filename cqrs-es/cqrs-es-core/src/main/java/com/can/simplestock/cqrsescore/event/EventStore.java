package com.can.simplestock.cqrsescore.event;

import com.can.simplestock.cqrsescore.messages.BaseEvent;

import java.util.List;

public interface EventStore {
    void saveEvents(String aggregateId, List<BaseEvent> events, int expectedVersion);
    List<BaseEvent> getEvents(String aggregateId);
}
