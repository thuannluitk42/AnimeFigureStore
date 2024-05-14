package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.SearchRequest;
import com.tpsolution.animestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductClientController {

    @Autowired
    ProductService productService;

    @GetMapping("/get-info-product/{productId}")
    public ResponseEntity<DataResponse> getInfoDetailProduct(@PathVariable String productId) {
        return ResponseEntity.ok().body(productService.getInfoDetailProduct(productId));
    }

    @GetMapping("/search-product")
    public ResponseEntity<DataResponse> getProductAll(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(productService.getProductAll(searchRequest));
    }
}
