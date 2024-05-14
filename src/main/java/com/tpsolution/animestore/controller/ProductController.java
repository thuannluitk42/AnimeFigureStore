package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.AddProductRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.SearchRequest;
import com.tpsolution.animestore.payload.UpdateProductRequest;
import com.tpsolution.animestore.service.ProductService;
import com.tpsolution.animestore.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    ProductService productService;
    @GetMapping("/get-info-product/{productId}")
    public ResponseEntity<DataResponse> getInfoDetailProduct(@PathVariable String productId) {
        return ResponseEntity.ok().body(productService.getInfoDetailProduct(productId));
    }

    @PostMapping(value ="/add-new-product", consumes = { "multipart/form-data" })
    public ResponseEntity<DataResponse> insertNewProduct(@RequestPart("data") AddProductRequest request,
                                                         @RequestParam("avatar") MultipartFile multipartFile) throws IOException {

        if (null != multipartFile && !multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            request.setImages(fileName);
            String uploadDir = "product-photos/";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            return ResponseEntity.ok(productService.insertNewProduct(request));

        } else {

            return ResponseEntity.ok(productService.insertNewProduct(request));
        }
    }

    @PostMapping(value = "/update-info-product", consumes = { "multipart/form-data" })
    public ResponseEntity<DataResponse> updateInfoUser(@RequestPart("data") UpdateProductRequest request,
                                                       @RequestParam("avatar") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            request.setImages(fileName);
            String uploadDir = "product-photos/";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            return ResponseEntity.ok(productService.updateProduct(request));
        } else {

            return ResponseEntity.ok(productService.updateProduct(request));
        }
    }
    @GetMapping("/search-product")
    public ResponseEntity<DataResponse> getProductAll(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(productService.getProductAll(searchRequest));
    }

    @GetMapping("/find-all-product")
    public ResponseEntity<DataResponse> findAllProduct() {
        return ResponseEntity.ok(productService.findAllProduct());
    }
}
