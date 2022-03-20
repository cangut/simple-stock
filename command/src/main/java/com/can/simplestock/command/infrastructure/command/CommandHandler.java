package com.can.simplestock.command.infrastructure.command;

import com.can.simplestock.command.api.commands.*;

public interface CommandHandler {
    void handle(OpenStockCommand command);
    void handle(IncreaseStockCommand command);
    void handle(DecreaseStockCommand command);
    void handle(CloseStockCommand command);
    void handle(RestoreCommand command);
}
