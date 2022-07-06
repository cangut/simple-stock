package com.can.simplestock.command.api.commands;

import com.can.simplestock.cqrsescore.messages.BaseCommand;
import lombok.Data;

@Data
public class DecreaseStockCommand extends BaseCommand {
    private final int amount;
}
