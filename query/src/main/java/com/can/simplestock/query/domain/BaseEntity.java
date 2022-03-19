package com.can.simplestock.query.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Getter
    protected String id;

    @Getter
    @Setter
    protected Date createdDate;
}
