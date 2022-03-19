package com.can.simplestock.query.domain;

import com.can.simplestock.common.constants.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Stock extends BaseEntity {
    private String productName;
    private String productCode;
    private ProductType productType;
    private int availableStock;
}
