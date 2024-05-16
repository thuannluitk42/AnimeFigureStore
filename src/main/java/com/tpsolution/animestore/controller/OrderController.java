package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.entity.KeyOrdersDetail;
import com.tpsolution.animestore.entity.OrderDetail;
import com.tpsolution.animestore.entity.Product;
import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.repository.OrderDetailRepository;
import com.tpsolution.animestore.repository.ProductRepository;
import com.tpsolution.animestore.service.OrderService;
import com.tpsolution.animestore.utils.ExcelGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/get-info-order/{orderId}")
    public ResponseEntity<DataResponse> getInfoDetailOrder(@PathVariable int orderId) {
        return ResponseEntity.ok().body(orderService.getInfoDetailOrder(orderId));
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

    @GetMapping("/list-order")
    public ResponseEntity<DataResponse> getAllOrder() {
        return ResponseEntity.ok().body(orderService.getAllOrder());
    }

    @GetMapping("/export-to-excel/{orderId}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable int orderId) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=HoaDon" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailByOrderId(orderId);
        List<DataOrderDetailResponse> dataOrderDetailResponses = new ArrayList<>();
        for (OrderDetail i: orderDetailList) {
            DataOrderDetailResponse item = new DataOrderDetailResponse();
            KeyOrdersDetail kod = i.getKeys();
            Product p = productRepository.findProductByProductId(kod.getProductId());

            item.setProductName(p.getProductName());
            item.setAmount(i.getAmount());
            item.setUnitPrice(i.getUnitPrice());
            item.setSubTotal(i.getSubTotal());

            dataOrderDetailResponses.add(item);
        }

        ExcelGenerator generator = new ExcelGenerator(dataOrderDetailResponses);
        generator.generateExcelFile(response);
    }
}
