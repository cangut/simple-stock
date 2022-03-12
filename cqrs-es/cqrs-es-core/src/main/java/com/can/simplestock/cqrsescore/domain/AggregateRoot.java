package com.can.simplestock.cqrsescore.domain;

import com.can.simplestock.cqrsescore.messages.BaseEvent;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AggregateRoot {

    @Getter
    protected String id;

    @Getter
    @Setter
    private int version = -1;
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    private final List<BaseEvent> domainEvents = new ArrayList<>();

    public void setDomainEventsAsConsumed() {
        domainEvents.clear();
    }

    public List<BaseEvent> getUnconsumedDomainEvents() {
        return this.domainEvents;
    }

    protected void applyChange(BaseEvent event, boolean isNew) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, MessageFormat.format("apply method was not found in the aggregate for {0}", event.getClass().getName()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred applying event");
        } finally {
            if (isNew) {
                domainEvents.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(List<BaseEvent> events) {
        events.forEach(event -> applyChange(event, false));
    }
}
