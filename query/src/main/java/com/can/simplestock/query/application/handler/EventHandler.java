package com.can.simplestock.query.application.handler;

import com.can.simplestock.common.events.StockClosedEvent;
import com.can.simplestock.common.events.StockDecreasedEvent;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;

public interface EventHandler {
    void on(StockOpenedEvent event);
    void on(StockIncreasedEvent event);
    void on(StockDecreasedEvent event);
    void on(StockClosedEvent event);
}
