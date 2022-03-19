package com.can.simplestock.query.api.queries;

import com.can.simplestock.common.constants.EqualityType;
import com.can.simplestock.cqrsescore.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindStockByAmount extends BaseQuery {
    private EqualityType equalityType;
    private int amount;
}
