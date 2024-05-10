package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.entity.Category;
import com.tpsolution.animestore.entity.Product;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.exception.NotFoundException;
import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.repository.CategoryRepository;
import com.tpsolution.animestore.repository.ProductCriteriaRepository;
import com.tpsolution.animestore.repository.ProductRepository;
import com.tpsolution.animestore.service.imp.ProductServiceImp;
import com.tpsolution.animestore.utils.CommonUtils;
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

import static com.tpsolution.animestore.repository.ProductCriteriaRepository.withProductSearch;

@Service
public class ProductService implements ProductServiceImp {
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductCriteriaRepository productCriteriaRepository;

    @Override
    @Transactional
    public DataResponse insertNewProduct(AddProductRequest request) {
        logger.info("#insertNewProduct productName: {}", request.getProduct_name());
        Product product = new Product();

        try {
            if (StringUtils.hasText(request.getProduct_name()) && CommonUtils.containsSpecialCharacter(request.getProduct_name())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_NAME_IS_INVALID);
            }

            if (StringUtils.hasText(request.getProduct_name()) == false) {
                throw new BadRequestException(ErrorMessage.PRODUCT_NAME_IS_NOT_NULL);
            }

            product.setProductName(request.getProduct_name());

            if (StringUtils.hasText(String.valueOf(request.getProduct_price())) && request.getProduct_price() < 0) {
                throw new BadRequestException(ErrorMessage.PRODUCT_PRICE_IS_INVALID);
            }

            if (StringUtils.hasText(String.valueOf(request.getProduct_price())) == false) {
                throw new BadRequestException(ErrorMessage.PRODUCT_PRICE_IS_INVALID);
            }

            product.setProductPrice(request.getProduct_price());

            if (StringUtils.hasText(request.getImages()) == false) {
                throw new BadRequestException(ErrorMessage.PRODUCT_IMAGES_IS_INVALID);
            }

            if (StringUtils.hasText(request.getImages()) && CommonUtils.isImageFile(request.getImages())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_IMAGES_IS_INVALID);
            }

            product.setProductImages(request.getImages());

            if (StringUtils.hasText(String.valueOf(request.getProduct_quantity())) && request.getProduct_quantity() < 0) {
                throw new BadRequestException(ErrorMessage.PRODUCT_QUANTITY_IS_INVALID);
            }

            if (StringUtils.hasText(String.valueOf(request.getProduct_quantity())) == false) {
                throw new BadRequestException(ErrorMessage.PRODUCT_PRICE_IS_INVALID);
            }

            product.setProductQuantity(request.getProduct_quantity());

            if (StringUtils.hasText(request.getProduct_discount())) {
                product.setDiscount(request.getProduct_discount());
            } else {
                product.setDiscount("");
            }

            Category category = categoryRepository.findCategoryByCategoryIdAndDeleted(request.getCategory_id(),Boolean.FALSE);

            if (null == category) {
                throw new BadRequestException(ErrorMessage.CATEGORY_IS_NOT_EXIST);
            } else {
                product.setCategory(category);
            }

            product = productRepository.save(product);

        } catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(product);
    }

    @Override
    @Transactional
    public DataResponse updateProduct(UpdateProductRequest request) {
        logger.info("#updateProduct productname: {}", request.getProduct_name());
        Product product = new Product();

        try {
            if (StringUtils.hasText(request.getProductId()) == true && !CommonUtils.isNumeric(request.getProductId())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_ID_IS_INVALID);
            }

            if (StringUtils.hasText(request.getProductId()) == false) {
                throw new BadRequestException(ErrorMessage.PRODUCT_ID_IS_INVALID);
            }

            product = productRepository.findProductByProductIdAndDeleted(Integer.valueOf(request.getProductId()), Boolean.FALSE);

            if (product == null) {
                throw new NotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
            } else {

                if (StringUtils.hasText(request.getProduct_name())) {
                    product.setProductName(request.getProduct_name());
                }

                if (StringUtils.hasText(String.valueOf(request.getProduct_price())) && request.getProduct_price() > 0) {
                    product.setProductPrice(request.getProduct_price());
                }

                if (StringUtils.hasText(request.getImages())) {
                    product.setProductImages(request.getImages());
                }

                if (StringUtils.hasText(String.valueOf(request.getProduct_quantity())) && request.getProduct_quantity() > 0) {
                    product.setProductQuantity(request.getProduct_quantity());
                }

                if (StringUtils.hasText(request.getProduct_description())) {
                    product.setProductDescription(request.getProduct_description());
                }

                if (StringUtils.hasText(request.getProduct_discount())) {
                    product.setDiscount(request.getProduct_description());
                }

                Category category = categoryRepository.findCategoryByCategoryIdAndDeleted(request.getCategory_id(),Boolean.FALSE);

                if (null != category) {
                    product.setCategory(category);
                }

                product.setDeleted(false);

                product = productRepository.save(product);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(product);
    }

    @Override
    public DataResponse getInfoDetailProduct(String productId) {
        logger.info("#getInfoDetailProduct: "+productId);
        Product product = productRepository.findProductByProductIdAndDeleted(Integer.valueOf(productId),Boolean.FALSE);

        if (null == product) {
            throw new NotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        DataProductResponse productData = new DataProductResponse();
        productData.setProductId(productId);
        productData.setProduct_name(product.getProductName());
        productData.setProduct_price(product.getProductPrice());
        productData.setProduct_quantity(product.getProductQuantity());
        productData.setProduct_discount(product.getDiscount());
        productData.setProduct_description(product.getProductDescription());
        productData.setImages(product.getProductImages());
        productData.setCategory_id(productData.getCategory_id());

        return DataResponse.ok(productData);
    }

    @Override
    @Transactional
    public DataResponse getProductAll(SearchRequest searchRequest) {
        logger.info("#getProductAll");
        if (searchRequest == null) {
            throw new BadRequestException(ErrorMessage.SEARCH_REQUEST_IS_NOT_NULL);
        }
        int page = searchRequest.getPage();
        if (page > 0) {
            page = page - 1;
        }
        Pageable pageableRequest = PageRequest.of(page, searchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        ProductResponse productResponse = new ProductResponse();

        Page<Product> productPage = productCriteriaRepository.findAll(buildSpecification(searchRequest), pageableRequest);
        if (!productPage.hasContent()) {
            logger.info("Query with empty data");
            productResponse.setList(Collections.emptyList());
            return DataResponse.ok(productResponse);
        }
        productResponse.setList(productPage.get().map(this::build).collect(Collectors.toList()));
        productResponse.setCurrentPage(searchRequest.getPage());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setTotalElement(productPage.getTotalElements());
        return DataResponse.ok(productResponse);
    }

    private Specification<Product> buildSpecification(SearchRequest searchRequest) {
        Specification<Product> spec = Specification.where(ProductCriteriaRepository.withIsDeleted(Boolean.FALSE));

        String search = searchRequest.getSearch();
        if (StringUtils.hasText(search)) {
            spec = spec.and(withProductSearch(search));
        }
        return spec;
    }

    public ProductDetailResponse build(Product product) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();

        productDetailResponse.setProductId(UUID.fromString(String.valueOf(product.getProductId())));
        productDetailResponse.setProduct_name(product.getProductName());
        productDetailResponse.setProduct_price(product.getProductPrice());
        productDetailResponse.setProduct_quantity(product.getProductQuantity());
        productDetailResponse.setProduct_description(product.getProductDescription());
        productDetailResponse.setImages(product.getProductImages());
        productDetailResponse.setCategory_id(product.getCategory().getCategoryId());
        productDetailResponse.setProduct_discount(product.getDiscount());

        return productDetailResponse;
    }
}
