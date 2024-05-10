package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.*;

public interface CategoryServiceImp {

    DataResponse insertNewCategory(AddCategoryRequest request);

    DataResponse updateCategory(UpdateCategoryRequest request);

    DataResponse getInfoDetailCategory(String categoryId);

    DataResponse getCategoryAll(SearchRequest searchRequest);
}
