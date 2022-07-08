package com.can.simplestock.cqrsescore.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
abstract class BaseEventModel {
    @Id
    private String id;
}
