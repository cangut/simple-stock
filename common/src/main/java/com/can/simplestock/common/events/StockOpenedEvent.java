package com.can.simplestock.common.events;

import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.cqrsescore.messages.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockOpenedEvent extends BaseEvent {
    private String productCode;
    private String productName;
    private ProductType productType;
    private String availableStock;
}
