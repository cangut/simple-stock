package com.can.simplestock.command.api.commands;

import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.cqrsescore.messages.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenStockCommand extends BaseCommand {
    private final String productCode;
    private final String productName;
    private final ProductType productType;
    private final int availableStock;
}
