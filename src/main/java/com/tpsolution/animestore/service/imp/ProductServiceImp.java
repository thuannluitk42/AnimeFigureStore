package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.*;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServiceImp {

    DataResponse insertNewProduct(AddProductRequest request, MultipartFile multipartFile);

    DataResponse updateProduct(UpdateProductRequest request);

    DataResponse getInfoDetailProduct(String productId);

    DataResponse getProductAll(SearchRequest searchRequest);

    DataResponse findAllProduct();

    DataResponse filterProduct(FilterProductRequest filterProductRequest);

    DataResponse showProductHomePage();

    DataResponse changeStatusProducts(DeleteIDsRequest request);
}
