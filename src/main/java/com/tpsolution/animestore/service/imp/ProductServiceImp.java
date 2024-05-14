package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.*;

public interface ProductServiceImp {

    DataResponse insertNewProduct(AddProductRequest request);

    DataResponse updateProduct(UpdateProductRequest request);

    DataResponse getInfoDetailProduct(String productId);

    DataResponse getProductAll(SearchRequest searchRequest);

    DataResponse findAllProduct();
}