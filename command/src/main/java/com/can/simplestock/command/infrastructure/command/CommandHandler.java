package com.can.simplestock.command.infrastructure.command;

import com.can.simplestock.command.api.commands.CloseStockCommand;
import com.can.simplestock.command.api.commands.DecreaseStockCommand;
import com.can.simplestock.command.api.commands.IncreaseStockCommand;
import com.can.simplestock.command.api.commands.OpenStockCommand;

public interface CommandHandler {
    void handle(OpenStockCommand command);
    void handle(IncreaseStockCommand command);
    void handle(DecreaseStockCommand command);
    void handle(CloseStockCommand command);
}
