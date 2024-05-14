package com.tpsolution.animestore.payload;

import com.tpsolution.animestore.dto.OrderDetailDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AddOrderRequest {
    private UUID userId;
    private double totalBill;
    private List<OrderDetailDTO> detailDTOList;

}
