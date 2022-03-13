package com.can.simplestock.cqrsescore.event;

import org.springframework.data.annotation.Id;

abstract class BaseEventModel {
    @Id
    private String id;
}
