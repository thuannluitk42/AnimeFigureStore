package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.entity.Order;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.exception.NotFoundException;
import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.repository.OrderCriteriaRepository;
import com.tpsolution.animestore.repository.OrderRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.OrderServiceImp;
import com.tpsolution.animestore.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceImp  {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OrderCriteriaRepository orderCriteriaRepository;
    @Override
    @Transactional
    public DataResponse insertNewOrder(AddOrderRequest request) {
        logger.info("#insertNewOrder");
        Order order = new Order();

        try {
            if (StringUtils.hasText(String.valueOf(request.getUserId())) && CommonUtils.isNumeric(String.valueOf(request.getUserId()))) {
                throw new BadRequestException(ErrorMessage.USER_ID_IS_INVALID);
            }

            if (StringUtils.hasText(String.valueOf(request.getUserId())) == false) {
                throw new BadRequestException(ErrorMessage.USER_ID_IS_NOT_NULL);
            }

            Users userEntity = usersRepository.getUsersByUserId(Integer.valueOf(String.valueOf(request.getUserId())));

            if (null == userEntity) {
                throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
            }

            order.setUsers(userEntity);
            order.setPaymentOption(0);
            order.setPaymentStatus(0);
            order.setDeliveryAddress(userEntity.getAddress());

            if (StringUtils.hasText(String.valueOf(request.getTotalBill())) && CommonUtils.isNumeric(String.valueOf(request.getTotalBill()))) {
                throw new BadRequestException(ErrorMessage.TOTAL_ORDER_IS_INVALID);
            }

            if (StringUtils.hasText(String.valueOf(request.getTotalBill())) == false) {
                order.setTotal(0);
            } else {
                order.setTotal(request.getTotalBill());
            }

            Date date = new Date();
            order.setCreatedDate(date);

            order = orderRepository.save(order);

        } catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(order);
    }

    @Override
    @Transactional
    public DataResponse updateOrder(UpdateOrderRequest request) {
        logger.info("#updateOrder orderId: {}", request.getOrderId());
        Order order = new Order();

        try {
            if (StringUtils.hasText(String.valueOf(request.getOrderId())) == true && !CommonUtils.isNumeric(String.valueOf(request.getOrderId()))) {
                throw new BadRequestException(ErrorMessage.ORDER_ID_IS_INVALID);
            }

            if (StringUtils.hasText(String.valueOf(request.getOrderId())) == false) {
                throw new BadRequestException(ErrorMessage.ORDER_ID_IS_NOT_NULL);
            }

            order = orderRepository.findOrderByOrderIdAndPaymentStatus(Integer.valueOf(String.valueOf(request.getOrderId())),0);

            if (order == null) {

                throw new NotFoundException(ErrorMessage.ORDER_NOT_FOUND);

            } else {

                if (StringUtils.hasText(request.getDeliveryAddress())) {
                    order.setDeliveryAddress(request.getDeliveryAddress());
                }

                if (StringUtils.hasText(String.valueOf(request.getPaymentOption())) && request.getPaymentOption() >= 1 && request.getPaymentOption() <= 2) {
                    order.setPaymentOption(request.getPaymentOption());
                }

                if (StringUtils.hasText(String.valueOf(request.getPaymentStatus())) && request.getPaymentStatus() >= 0 && request.getPaymentOption() <= 2) {
                    order.setPaymentStatus(request.getPaymentStatus());
                }

                if (StringUtils.hasText(String.valueOf(request.getTotalBill())) && request.getTotalBill() > 0) {
                    order.setTotal(request.getTotalBill());
                }

                if (StringUtils.hasText(String.valueOf(request.getVnpayTransactionId()))) {
                    order.setVnpayTransactionId(request.getVnpayTransactionId());
                }

                order = orderRepository.save(order);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(order);
    }

    @Override
    public DataResponse getInfoDetailOrder(String orderId) {
        logger.info("#getInfoDetailOrder: "+orderId);
        Order order = orderRepository.findOrderByOrderId(Integer.valueOf(orderId));

        if (null == order) {
            throw new NotFoundException(ErrorMessage.ORDER_NOT_FOUND);
        }

        DataOrderResponse orderData = new DataOrderResponse();
        orderData.setOrderId(UUID.fromString(orderId));

        Users userEntity = usersRepository.getUsersByUserId(Integer.valueOf(String.valueOf(order.getUsers().getUserId())));

        if (null == userEntity) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        orderData.setUsers(userEntity);

        orderData.setTotalBill(order.getTotal());
        orderData.setPaymentOption(order.getPaymentOption());
        orderData.setPaymentStatus(order.getPaymentStatus());
        orderData.setVnpayTransactionId(order.getVnpayTransactionId());
        orderData.setCreatedDay(order.getCreatedDate());

        return DataResponse.ok(orderData);
    }

    @Override
    @Transactional
    public DataResponse getOrderAll(SearchRequest searchRequest) {
        logger.info("#getOrderAll");
        if (searchRequest == null) {
            throw new BadRequestException(ErrorMessage.SEARCH_REQUEST_IS_NOT_NULL);
        }
        int page = searchRequest.getPage();
        if (page > 0) {
            page = page - 1;
        }
        Pageable pageableRequest = PageRequest.of(page, searchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        OrderResponse orderResponse = new OrderResponse();

        Page<Order> orderPage = orderCriteriaRepository.findAll(pageableRequest);
        if (!orderPage.hasContent()) {
            logger.info("Query with empty data");
            orderResponse.setList(Collections.emptyList());
            return DataResponse.ok(orderResponse);
        }
        orderResponse.setList(orderPage.get().map(this::build).collect(Collectors.toList()));
        orderResponse.setCurrentPage(searchRequest.getPage());
        orderResponse.setTotalPage(orderPage.getTotalPages());
        orderResponse.setTotalElement(orderPage.getTotalElements());
        return DataResponse.ok(orderResponse);
    }

    public DataOrderResponse build(Order order) {
        DataOrderResponse orderData = new DataOrderResponse();
        orderData.setOrderId(UUID.fromString(String.valueOf(order.getOrderId())));

        Users userEntity = usersRepository.getUsersByUserId(Integer.valueOf(String.valueOf(order.getUsers().getUserId())));

        if (null == userEntity) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        orderData.setUsers(userEntity);

        orderData.setTotalBill(order.getTotal());
        orderData.setPaymentOption(order.getPaymentOption());
        orderData.setPaymentStatus(order.getPaymentStatus());
        orderData.setVnpayTransactionId(order.getVnpayTransactionId());
        orderData.setCreatedDay(order.getCreatedDate());

        return orderData;
    }
}
