package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.*;

public interface OrderServiceImp {

    DataResponse insertNewOrder(AddOrderRequest request);

    DataResponse updateOrder(UpdateOrderRequest request);

    DataResponse getInfoDetailOrder(int orderId);

    DataResponse getOrderAll(SearchRequest searchRequest);

    DataResponse getInfo4OldBillYesterday();

    DataResponse getInfo2OldBillToday();

    DataResponse getAllOrder();
}
