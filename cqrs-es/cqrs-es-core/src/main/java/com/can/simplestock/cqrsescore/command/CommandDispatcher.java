package com.can.simplestock.cqrsescore.command;

import com.can.simplestock.cqrsescore.messages.BaseCommand;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> classType, CommandHandlerMethod<T> handlerMethod);

    void send(BaseCommand command);
}
