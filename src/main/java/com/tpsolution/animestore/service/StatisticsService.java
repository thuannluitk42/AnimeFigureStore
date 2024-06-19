package com.tpsolution.animestore.service;

import com.tpsolution.animestore.dto.*;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.repository.OrderDetailRepository;
import com.tpsolution.animestore.repository.OrderRepository;
import com.tpsolution.animestore.repository.ProductRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.StatisticsServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticsService implements StatisticsServiceImpl {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public DataResponse getStatistics() {
        StatisticsDTO statisticsDTO = new StatisticsDTO();

        // Total number of orders
        statisticsDTO.setTotalOrders(orderRepository.count());

        // Total revenue
        statisticsDTO.setTotalRevenue(orderRepository.sumTotalRevenue());

        // Average order value
        statisticsDTO.setAverageOrderValue(orderRepository.averageOrderValue());

        // Number of products sold per category
        statisticsDTO.setProductsSoldPerCategory(orderDetailRepository.countProductsSoldPerCategory());

        // Number of active users
        statisticsDTO.setActiveUsers(userRepository.countActiveUsers());

        return DataResponse.ok(statisticsDTO);
    }

    @Override
    public DataResponse getMonthlyRevenue() {
        return DataResponse.ok(orderRepository.findMonthlyRevenue().stream()
                .map(result -> new MonthlyRevenueDTO((Integer) result[0], (Integer) result[1], (Double) result[2]))
                .collect(Collectors.toList()));
    }

    @Override
    public DataResponse getQuarterlyRevenue() {
        return DataResponse.ok(orderRepository.findQuarterlyRevenue().stream()
                .map(result -> new QuarterlyRevenueDTO((Integer) result[0], (Integer) result[1], (Double) result[2]))
                .collect(Collectors.toList()));
    }

    @Override
    public DataResponse getYearlyRevenue() {
        return DataResponse.ok(orderRepository.findYearlyRevenue().stream()
                .map(result -> new YearlyRevenueDTO((Integer) result[0], (Double) result[1]))
                .collect(Collectors.toList()));
    }

    @Override
    public DataResponse getMonthlyProductsSold() {
        return DataResponse.ok(orderDetailRepository.findMonthlyProductsSold().stream()
                .map(result -> new MonthlyProductsSoldDTO((Integer) result[0], (Integer) result[1], ((Number) result[2]).intValue()))
                .collect(Collectors.toList()));
    }

    @Override
    public DataResponse getQuarterlyProductsSold() {
        return DataResponse.ok(orderDetailRepository.findQuarterlyProductsSold().stream()
                .map(result -> new QuarterlyProductsSoldDTO((Integer) result[0], (Integer) result[1], ((Number) result[2]).intValue()))
                .collect(Collectors.toList()));
    }

    @Override
    public DataResponse getYearlyProductsSold() {
        return DataResponse.ok(orderDetailRepository.findYearlyProductsSold().stream()
                .map(result -> new YearlyProductsSoldDTO((Integer) result[0], ((Number) result[1]).intValue()))
                .collect(Collectors.toList()));
    }
}
