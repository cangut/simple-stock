package com.can.simplestock.cqrsescore.event;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
abstract class BaseEventModel {
    @Id
    private String id;
}
