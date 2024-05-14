package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.AddOrderRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.SearchRequest;
import com.tpsolution.animestore.payload.UpdateOrderRequest;
import com.tpsolution.animestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/get-info-order/{orderId}")
    public ResponseEntity<DataResponse> getInfoDetailOrder(@PathVariable String orderIdId) {
        return ResponseEntity.ok().body(orderService.getInfoDetailOrder(orderIdId));
    }

    @PostMapping(value ="/add-new-order")
    public ResponseEntity<DataResponse> insertNewOrder(@RequestBody AddOrderRequest request) {
        return ResponseEntity.ok(orderService.insertNewOrder(request));
    }

    @PostMapping(value = "/update-info-order")
    public ResponseEntity<DataResponse> updateInfoOrder(@RequestBody UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(request));
    }
    @GetMapping("/search-order")
    public ResponseEntity<DataResponse> getOrderAll(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(orderService.getOrderAll(searchRequest));
    }

    @GetMapping("/get-4-old-bill-yesterday")
    public ResponseEntity<DataResponse> getInfo4OldBillYesterday() {
        return ResponseEntity.ok().body(orderService.getInfo4OldBillYesterday());
    }

    @GetMapping("/get-2-new-bill-today")
    public ResponseEntity<DataResponse> getInfo2OldBillToday() {
        return ResponseEntity.ok().body(orderService.getInfo2OldBillToday());
    }
}
