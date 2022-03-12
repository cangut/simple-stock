package com.can.simplestock.command.api.commands;

import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.cqrsescore.messages.BaseCommand;
import lombok.Data;

@Data
public class OpenStockCommand extends BaseCommand {
    private String productCode;
    private String productName;
    private ProductType productType;
    private String availableStock;
}
