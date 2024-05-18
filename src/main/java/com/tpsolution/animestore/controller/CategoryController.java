package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.AddCategoryRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.SearchRequest;
import com.tpsolution.animestore.payload.UpdateCategoryRequest;
import com.tpsolution.animestore.service.imp.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryServiceImp categoryService;

    @GetMapping("/get-info-category/{categoryId}")
    public ResponseEntity<DataResponse> getInfoDetailCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok().body(categoryService.getInfoDetailCategory(categoryId));
    }

    @PostMapping("/add-new-category")
    public ResponseEntity<DataResponse> insertNewCategory(@RequestBody AddCategoryRequest request) {
        return ResponseEntity.ok(categoryService.insertNewCategory(request));
    }

    @PostMapping(value = "/update-info-category")
    public ResponseEntity<DataResponse> updateInfoUser(@RequestBody UpdateCategoryRequest request) throws IOException {
            return ResponseEntity.ok(categoryService.updateCategory(request));
    }

    @GetMapping("/search-category")
    public ResponseEntity<DataResponse> getCategoryAll(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(categoryService.getCategoryAll(searchRequest));
    }

    @GetMapping("/findAllCategory")
    public ResponseEntity<DataResponse> getCategoryAll() {
        return ResponseEntity.ok(categoryService.findAllCategory());
    }
}
