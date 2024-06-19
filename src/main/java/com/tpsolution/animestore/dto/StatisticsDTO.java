package com.tpsolution.animestore.dto;

import lombok.Data;

@Data
public class StatisticsDTO {
    private long totalOrders;
    private double totalRevenue;
    private double averageOrderValue;
    private long productsSoldPerCategory;
    private long activeUsers;
}
