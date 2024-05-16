package com.tpsolution.animestore.payload;

import com.tpsolution.animestore.dto.OrderDetailDTO;
import lombok.Data;

import java.util.List;

@Data
public class AddOrderRequest {
    private int userId;
    private double totalBill;
    private List<OrderDetailDTO> detailDTOList;
}
