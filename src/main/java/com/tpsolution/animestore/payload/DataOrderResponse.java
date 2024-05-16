package com.tpsolution.animestore.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tpsolution.animestore.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataOrderResponse {
    private int orderId;
    private int userId;
    private int paymentOption;
    private int paymentStatus;
    private int vnpayTransactionId ;
    private double totalBill;
    private Date createdDay ;
    private String username;
    private List<DataOrderDetailResponse> listOrderDetail;
}
