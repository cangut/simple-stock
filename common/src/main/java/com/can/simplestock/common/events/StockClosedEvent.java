package com.can.simplestock.common.events;

import com.can.simplestock.cqrsescore.messages.BaseEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class StockClosedEvent extends BaseEvent {
}
