package com.can.simplestock.query.api.responses;

import com.can.simplestock.common.dto.BaseResponse;
import com.can.simplestock.query.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QueryResponse extends BaseResponse {
    private List<Stock> stockList;

    public QueryResponse(String message) {
        super(message);
    }
}
