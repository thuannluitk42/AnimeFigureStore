package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.entity.Product;
import com.tpsolution.animestore.payload.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductServiceImp {

    DataResponse insertNewProduct(AddProductRequest request, MultipartFile multipartFile) throws IOException;

    DataResponse updateProduct(UpdateProductRequest request);

    DataResponse getInfoDetailProduct(String productId);

    DataResponse getProductAll(SearchRequest searchRequest);

    DataResponse findAllProduct();

    DataResponse filterProduct(FilterProductRequest filterProductRequest);

    DataResponse showProductHomePage();

    DataResponse changeStatusProducts(DeleteIDsRequest request);

    DataResponse updateProductPrice(Long productId, Double newPrice);

    void applyFlashSale(List<Integer> productIds, double discountPercentage);

    void revertFlashSale(List<Integer> productIds, double discountPercentage);
    void uploadNewProducts(List<Product> products);

    void deactivateOutOfStockProducts();
}
