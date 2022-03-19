package com.can.simplestock.query.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@SuperBuilder
public abstract class BaseEntity {

    @Id
    @Getter
    protected String id;

    @Getter
    @Setter
    protected Date createdDate;
}
