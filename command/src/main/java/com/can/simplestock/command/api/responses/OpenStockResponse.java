package com.can.simplestock.command.api.responses;

import com.can.simplestock.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenStockResponse extends BaseResponse {

    private String id;

    public OpenStockResponse(String message, String id) {
        super(message);
        this.id = id;
    }
}
