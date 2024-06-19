package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.DataResponse;

public interface StatisticsServiceImpl {
    DataResponse getStatistics();

    DataResponse getMonthlyRevenue();

    DataResponse getQuarterlyRevenue();

    DataResponse getYearlyRevenue();

    DataResponse getMonthlyProductsSold();

    DataResponse getQuarterlyProductsSold();

    DataResponse getYearlyProductsSold();
}
