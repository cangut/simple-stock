package com.can.simplestock.command.api.commands;

import com.can.simplestock.cqrsescore.messages.BaseCommand;
import lombok.Data;

@Data
public class IncreaseStockCommand extends BaseCommand {
    private int amount;
}
