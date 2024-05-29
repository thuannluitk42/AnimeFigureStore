package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse extends PagingResponse {
    private List<DataOrderResponse> list;
}
