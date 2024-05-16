package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.dto.OrderDetailDTO;
import com.tpsolution.animestore.entity.*;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.exception.NotFoundException;
import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.repository.*;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceImp  {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderCriteriaRepository orderCriteriaRepository;
    @Override
    @Transactional
    public DataResponse insertNewOrder(AddOrderRequest request) {
        logger.info("#insertNewOrder: "+request.getUserId());
        Order order = new Order();

            if (StringUtils.hasText(String.valueOf(request.getUserId())) && !CommonUtils.isNumeric(String.valueOf(request.getUserId()))) {
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

            if (StringUtils.hasText(String.valueOf(request.getTotalBill())) && !CommonUtils.isNumeric(String.valueOf(request.getTotalBill()))) {
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

            for (OrderDetailDTO item:request.getDetailDTOList()) {
                OrderDetail orderDetail = new OrderDetail();

                Product product = productRepository.findProductByProductIdAndDeleted(item.getProductId(), Boolean.FALSE);

                if (product == null) {
                    throw new NotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
                }

                KeyOrdersDetail keyOrdersDetail = new KeyOrdersDetail(order.getOrderId(), userEntity.getUserId());
                orderDetail.setKeys(keyOrdersDetail);
                orderDetail.setAmount(item.getAmount());
                orderDetail.setUnitPrice(item.getUnitPrice());
                orderDetail.setSubTotal(item.getSubTotal());

                orderDetailRepository.save(orderDetail);
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

    @Override
    public DataResponse getInfo4OldBillYesterday() {
        logger.info("#getInfo4OldBillYesterday");

        LocalDate currentDate = LocalDate.now();
        LocalDate yesterdayDate = currentDate.minusDays(1);
        List<Order> orderList = orderRepository.get4OrdersWithHighestTotalBillYesterday(yesterdayDate);

        List<DataOrderResponse> dataOrderResponses = new ArrayList<>();

        if (orderList.size() > 0 ) {
            DataOrderResponse orderData = new DataOrderResponse();
            for (Order item:orderList) {
                orderData.setOrderId(UUID.fromString(String.valueOf(item.getOrderId())));
                orderData.setUsers(item.getUsers());
                orderData.setPaymentOption(item.getPaymentOption());
                orderData.setPaymentStatus(item.getPaymentStatus());
                orderData.setTotalBill(item.getTotal());
                orderData.setCreatedDay(item.getCreatedDate());
                dataOrderResponses.add(orderData);
            }
        }
        return DataResponse.ok(dataOrderResponses);
    }
    @Override
    public DataResponse getInfo2OldBillToday() {
        logger.info("#getInfo2OldBillToday");
        LocalDate currentDate = LocalDate.now();

        List<Order> orderList = orderRepository.get2OrdersWithHighestTotalBillToday(currentDate);
        List<DataOrderResponse> dataOrderResponses = new ArrayList<>();

        if (orderList.size() > 0 ) {
            DataOrderResponse orderData = new DataOrderResponse();
            for (Order item:orderList) {
                orderData.setOrderId(UUID.fromString(String.valueOf(item.getOrderId())));
                orderData.setUsers(item.getUsers());
                orderData.setPaymentOption(item.getPaymentOption());
                orderData.setPaymentStatus(item.getPaymentStatus());
                orderData.setTotalBill(item.getTotal());
                orderData.setCreatedDay(item.getCreatedDate());

                dataOrderResponses.add(orderData);
            }
        }
        return DataResponse.ok(dataOrderResponses);
    }

    @Override
    public DataResponse getAllOrder() {
        logger.info("#getAllOrder");
        List<Order> orderList = (List<Order>) orderRepository.findAll();
        List<DataOrderResponse> dataOrderResponses = new ArrayList<>();

        if (orderList.size() == 0){
            logger.info("data order is empty");
        } else {
            for (Order item :orderList) {
                DataOrderResponse orderResponse = new DataOrderResponse();
                orderResponse.setOrderId(UUID.fromString(String.valueOf(item.getOrderId())));
                orderResponse.setUsers(item.getUsers());
                orderResponse.setUsername(item.getUsers().getUsername());
                orderResponse.setTotalBill(item.getTotal());
                orderResponse.setCreatedDay(item.getCreatedDate());
                orderResponse.setListOrderDetail(getListOrderDetailById(item.getOrderId()));

                dataOrderResponses.add(orderResponse);
            }

        }
        return DataResponse.ok(dataOrderResponses);
    }

    private List<DataOrderDetailResponse> getListOrderDetailById (int orderId) {
        logger.info("#getListOrderDetailById: "+orderId);
        List<OrderDetail> listOrderDetail = orderDetailRepository.findOrderDetailByOrderId(orderId);
        List<DataOrderDetailResponse> listOrderDetailResponses = null;

        if (listOrderDetail.size() == 0){
            logger.info("#getListOrderDetailById: "+orderId + "is empty data");
        } else {
            for (OrderDetail od : listOrderDetail) {
                DataOrderDetailResponse item = new DataOrderDetailResponse();
                Product p = productRepository.findProductByProductId(1);
                item.setProductName(p.getProductName());
                item.setAmount(od.getAmount());
                item.setUnitPrice(od.getUnitPrice());
                item.setSubTotal(od.getSubTotal());

                listOrderDetailResponses.add(item);
            }
        }
        return listOrderDetailResponses;

    }

}
