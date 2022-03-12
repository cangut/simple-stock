package com.can.simplestock.common.events;

import com.can.simplestock.cqrsescore.messages.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockIncreasedEvent extends BaseEvent {
    private String amount;
}
