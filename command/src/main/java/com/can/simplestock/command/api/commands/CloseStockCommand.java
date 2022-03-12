package com.can.simplestock.command.api.commands;

import com.can.simplestock.cqrsescore.messages.BaseCommand;
import lombok.Data;

@Data
public class CloseStockCommand extends BaseCommand {

    public CloseStockCommand(String id) {
        super(id);
    }
}
