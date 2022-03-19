package com.can.simplestock.command.infrastructure.command;

import com.can.simplestock.cqrsescore.command.CommandDispatcher;
import com.can.simplestock.cqrsescore.command.CommandHandlerMethod;
import com.can.simplestock.cqrsescore.messages.BaseCommand;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> handlers = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> classType, CommandHandlerMethod<T> handlerMethod) {
        var handler = handlers.computeIfAbsent(classType, c -> new ArrayList<>());
        handler.add(handlerMethod);
    }

    @Override
    public void send(BaseCommand command) {
        var handler = handlers.get(command.getClass());

        if (handler == null || handlers.size() == 0) {
            throw new RuntimeException("No command handler defined for this command");
        }

        if (handler.size() > 1) {
            throw new RuntimeException("Command has to have only one handler");
        }

        handler.get(0).handle(command);
    }
}
