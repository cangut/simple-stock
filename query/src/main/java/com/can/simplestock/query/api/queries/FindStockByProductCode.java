package com.can.simplestock.query.api.queries;

import com.can.simplestock.cqrsescore.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindStockByProductCode extends BaseQuery {
    private String productCode;
}
