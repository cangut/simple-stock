package com.can.simplestock.query.domain;

import com.can.simplestock.cqrsescore.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractStockEntity extends BaseEntity {

    @Id
    @Getter
    protected String id;

    @Getter
    @Setter
    protected Date createdDate;
}
