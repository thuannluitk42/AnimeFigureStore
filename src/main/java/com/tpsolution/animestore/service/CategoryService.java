package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.entity.Category;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.exception.NotFoundException;
import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.repository.CategoryCriteriaRepository;
import com.tpsolution.animestore.repository.CategoryRepository;
import com.tpsolution.animestore.service.imp.CategoryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tpsolution.animestore.repository.CategoryCriteriaRepository.withCategoryNameSearch;

@Service
public class CategoryService implements CategoryServiceImp {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private CategoryCriteriaRepository categoryCriteriaRepository;
    private static final Logger logger = LogManager.getLogger(CategoryService.class);
    @Override
    @Transactional
    public DataResponse insertNewCategory(AddCategoryRequest request) {
        logger.info("#insertNewCategory categoryName: {}", request.getCategoryName());

        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDeleted(false);

        category = categoryRepository.save(category);

        return DataResponse.ok(category);
    }

    @Override
    @Transactional
    public DataResponse updateCategory(UpdateCategoryRequest request) {
        logger.info("#updateCategory categoryName: {}", request.getCategoryName());
        Category category = new Category();
        try {
            if (StringUtils.hasText(request.getCategoryName()) == false) {
                throw new BadRequestException(ErrorMessage.CATEGORY_NAME_IS_INVALID);
            }

            category = categoryRepository.findCategoriesByCategoryIdAndIsDelete(Integer.valueOf(request.getCategoryId()), Boolean.FALSE);

            if (category == null) {
                throw new NotFoundException(ErrorMessage.CATEGORY_NOT_FOUND);
            } else {

                if (StringUtils.hasText(request.getCategoryName())) {
                    category.setCategoryName(request.getCategoryName());
                } else {
                    category.setCategoryName(request.getCategoryName());
                }

                category.setDeleted(false);

                category = categoryRepository.save(category);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(category);
    }

    @Override
    @Transactional
    public DataResponse getInfoDetailCategory(String categoryId) {
        logger.info("#getInfoDetailCategory: "+categoryId);
        Category category = categoryRepository.findCategoriesByCategoryIdAndIsDelete(Integer.valueOf(categoryId),Boolean.FALSE);

        if (category == null) {
            throw new NotFoundException(ErrorMessage.CATEGORY_NOT_FOUND);
        }

        DataCategoryResponse dataCategoryResponse = new DataCategoryResponse();

        dataCategoryResponse.setCategoryId(category.getCategoryId());
        dataCategoryResponse.setCategoryName(category.getCategoryName());

        return DataResponse.ok(dataCategoryResponse);
    }

    @Override
    public DataResponse getCategoryAll(SearchRequest searchRequest) {
        logger.info("#getCategoryAll");
        if (searchRequest == null) {
            throw new BadRequestException(ErrorMessage.CATEGORY_REQUEST_IS_NOT_NULL);
        }
        int page = searchRequest.getPage();
        if (page > 0) {
            page = page - 1;
        }
        Pageable pageableRequest = PageRequest.of(page, searchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        CategoryResponse categoryResponse = new CategoryResponse();

        Page<Category> categoryPage = categoryCriteriaRepository.findAll(buildSpecification(searchRequest), pageableRequest);
        if (!categoryPage.hasContent()) {
            logger.info("Query with empty data");
            categoryResponse.setList(Collections.emptyList());
            return DataResponse.ok(categoryResponse);
        }
        categoryResponse.setList(categoryPage.get().map(this::build).collect(Collectors.toList()));
        categoryResponse.setCurrentPage(searchRequest.getPage());
        categoryResponse.setTotalPage(categoryPage.getTotalPages());
        categoryResponse.setTotalElement(categoryPage.getTotalElements());
        return DataResponse.ok(categoryResponse);
    }

    private Specification<Category> buildSpecification(SearchRequest searchRequest) {
        Specification<Category> spec = Specification.where(CategoryCriteriaRepository.withIsDeleted(Boolean.FALSE));

        String search = searchRequest.getSearch();
        if (StringUtils.hasText(search)) {
            spec = spec.and(withCategoryNameSearch(search));
        }
        return spec;
    }

    public CategoryDetailResponse build(Category category) {
        CategoryDetailResponse categoryDetailResponse = new CategoryDetailResponse();

        categoryDetailResponse.setCategoryId(UUID.fromString(String.valueOf(category.getCategoryId())));
        categoryDetailResponse.setCategoryName(category.getCategoryName());

        return categoryDetailResponse;
    }
}
