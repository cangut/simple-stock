package com.can.simplestock.cqrsescore.exceptions;

public class AggregateNotFoundException extends RuntimeException {

    public AggregateNotFoundException(String message) {
        super(message);
    }
}

