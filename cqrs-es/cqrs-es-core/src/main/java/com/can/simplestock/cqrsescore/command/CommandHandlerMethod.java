package com.can.simplestock.cqrsescore.command;

import com.can.simplestock.cqrsescore.messages.BaseCommand;

@FunctionalInterface
public interface CommandHandlerMethod<T extends BaseCommand> {
    void handle(T command);
}
