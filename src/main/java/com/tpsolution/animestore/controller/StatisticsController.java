package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.service.imp.StatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsServiceImpl statisticsService;

    @GetMapping
    public ResponseEntity<DataResponse> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<DataResponse> getMonthlyRevenue() {
        return ResponseEntity.ok(statisticsService.getMonthlyRevenue());
    }

    @GetMapping("/quarterly-revenue")
    public ResponseEntity<DataResponse> getQuarterlyRevenue() {
        return ResponseEntity.ok(statisticsService.getQuarterlyRevenue());
    }

    @GetMapping("/yearly-revenue")
    public ResponseEntity<DataResponse> getYearlyRevenue() {
        return ResponseEntity.ok(statisticsService.getYearlyRevenue());
    }

}
