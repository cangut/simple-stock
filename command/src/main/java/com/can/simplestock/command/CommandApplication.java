package com.can.simplestock.command;

import com.can.simplestock.command.api.commands.CloseStockCommand;
import com.can.simplestock.command.api.commands.DecreaseStockCommand;
import com.can.simplestock.command.api.commands.IncreaseStockCommand;
import com.can.simplestock.command.api.commands.OpenStockCommand;
import com.can.simplestock.command.infrastructure.command.CommandHandler;
import com.can.simplestock.cqrsescore.command.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {

    private final CommandDispatcher commandDispatcher;
    private final CommandHandler commandHandler;

    @Autowired
    public CommandApplication(CommandDispatcher commandDispatcher, CommandHandler commandHandler) {
        this.commandDispatcher = commandDispatcher;
        this.commandHandler = commandHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }

	@PostConstruct
    public void registerHandlers() {
		commandDispatcher.registerHandler(OpenStockCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(IncreaseStockCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(DecreaseStockCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(CloseStockCommand.class, commandHandler::handle);
    }

}
