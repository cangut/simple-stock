package com.can.simplestock.query.domain;

import com.can.simplestock.common.constants.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Stock extends BaseEntity {
    private String productName;
    private String productCode;
    private ProductType productType;
    private int availableStock;
}
