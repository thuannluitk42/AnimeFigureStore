package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.FilterProductRequest;
import com.tpsolution.animestore.service.imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/product")
public class ProductClientController {

    @Autowired
    ProductServiceImp productService;

    @PostMapping("/filter-product")
    public ResponseEntity<DataResponse> filterProduct(@RequestBody FilterProductRequest filterProductRequest) {
        return ResponseEntity.ok(productService.filterProduct(filterProductRequest));
    }

    @GetMapping("/show-product-home-page")
    public ResponseEntity<DataResponse> showProductHomePage() {
        return ResponseEntity.ok(productService.showProductHomePage());
    }

}
