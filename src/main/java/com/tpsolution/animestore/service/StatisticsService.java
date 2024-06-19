package com.tpsolution.animestore.service;

import com.tpsolution.animestore.dto.StatisticsDTO;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.repository.OrderDetailRepository;
import com.tpsolution.animestore.repository.OrderRepository;
import com.tpsolution.animestore.repository.ProductRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.StatisticsServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
