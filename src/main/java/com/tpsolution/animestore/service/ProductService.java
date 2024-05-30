package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.dto.CategoryDTO;
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
import com.tpsolution.animestore.utils.FileUploadUtil;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.tpsolution.animestore.repository.ProductCriteriaRepository.*;

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
    public DataResponse insertNewProduct(AddProductRequest request , MultipartFile multipartFile) throws IOException {
        logger.info("#insertNewProduct productName: {}", request.getProduct_name());
        Product product = new Product();

            if (StringUtils.hasText(request.getProduct_name()) && CommonUtils.containsSpecialCharacter(request.getProduct_name())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_NAME_IS_INVALID);
            }

            if (!StringUtils.hasText(request.getProduct_name())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_NAME_IS_NOT_NULL);
            }

            product.setProductName(request.getProduct_name());

            if (StringUtils.hasText(String.valueOf(request.getProduct_price())) && request.getProduct_price() < 0) {
                throw new BadRequestException(ErrorMessage.PRODUCT_PRICE_IS_INVALID);
            }

            if (!StringUtils.hasText(String.valueOf(request.getProduct_price()))) {
                throw new BadRequestException(ErrorMessage.PRODUCT_PRICE_IS_INVALID);
            }

            product.setProductPrice(request.getProduct_price());

            if (!StringUtils.hasText(request.getUrlImage())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_IMAGES_IS_INVALID);
            }

            if (StringUtils.hasText(request.getUrlImage()) && !CommonUtils.isImageFile(request.getUrlImage())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_IMAGES_IS_INVALID);
            }

            product.setProductImages(request.getUrlImage());

            if (StringUtils.hasText(String.valueOf(request.getProduct_quantity())) && request.getProduct_quantity() < 0) {
                throw new BadRequestException(ErrorMessage.PRODUCT_QUANTITY_IS_INVALID);
            }

            if (!StringUtils.hasText(String.valueOf(request.getProduct_quantity()))) {
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

            Date createDate = new Date();
            product.setCreatedDate(createDate);

            product = productRepository.save(product);

            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                String uploadDir = "product-photos/"+product.getProductId();
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }

        return DataResponse.ok(product.getProductId());
    }

    @Override
    @Transactional
    public DataResponse updateProduct(UpdateProductRequest request) {
        logger.info("#updateProduct productname: {}", request.getProduct_name());
        Product product = new Product();

        try {
            if (StringUtils.hasText(request.getProductId()) && !CommonUtils.isNumeric(request.getProductId())) {
                throw new BadRequestException(ErrorMessage.PRODUCT_ID_IS_INVALID);
            }

            if (!StringUtils.hasText(request.getProductId())) {
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

                if (StringUtils.hasText(request.getUrlImage())) {
                    product.setProductImages(request.getUrlImage());
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

                Date createDate = new Date();
                product.setCreatedDate(createDate);

                product.setDeleted(false);

                productRepository.save(product);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok("");
    }

    @Override
    public DataResponse getInfoDetailProduct(String productId) {
        logger.info("#getInfoDetailProduct: "+productId);
        Product product = productRepository.findProductByProductIdAndDeleted(Integer.valueOf(productId),Boolean.FALSE);

        if (null == product) {
            throw new NotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        ProductDetailResponse productData = build(product);
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
        Pageable pageableRequest = PageRequest.of(page, searchRequest.getSize(), Sort.by(Sort.Direction.ASC, "createdDate"));
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

    @Override
    public DataResponse findAllProduct() {
        List<ProductDetailResponse> list = new ArrayList<>();
        for (Product p:productRepository.findAll()) {
            ProductDetailResponse productDetailResponse = build(p);
            list.add(productDetailResponse);
        }
        if (list.isEmpty()) {
            logger.info("findAllProduct empty list");
            return DataResponse.ok(list);
        }
        return DataResponse.ok(list);
    }

    @Override
    public DataResponse filterProduct(FilterProductRequest filterProductRequest) {
        logger.info("#filterProduct");

        int page = filterProductRequest.getPage();
        if (page > 0) {
            page = page - 1;
        }
        Pageable pageableRequest;
        try {
            int sortBy = Integer.parseInt(String.valueOf(filterProductRequest.getSortBy()));

            switch(sortBy) {
                case 1:
                     pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.ASC, "productPrice"));
                    break;
                case 2:
                     pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.DESC, "productPrice"));
                    break;
                case 3:
                    pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.ASC, "productName"));
                    break;
                case 4:
                    pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.DESC, "productName"));
                    break;
                case 5:
                    pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.ASC, "createdDate"));
                    break;
                case 6:
                    pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdDate"));

                    break;
                case 7:
                    pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.DESC, "productQuantity"));
                    break;
//                default:
//                    spec = spec.and(withIsSortByProductQuantitiesDESC());
                default:
                    pageableRequest = PageRequest.of(page, filterProductRequest.getSize(), Sort.by(Sort.Direction.ASC, "productPrice"));
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorMessage.SORT_BY_IS_INVALID);
        }


        ProductResponse productResponse = new ProductResponse();

        Page<Product> productPage = productCriteriaRepository.findAll(buildFilterSpecification(filterProductRequest), pageableRequest);

        if (!productPage.hasContent()) {
            logger.info("Query with empty data");
            productResponse.setList(Collections.emptyList());
            return DataResponse.ok(productResponse);
        }

        productResponse.setList(productPage.get().map(this::build).collect(Collectors.toList()));
        productResponse.setCurrentPage(filterProductRequest.getPage());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setTotalElement(productPage.getTotalElements());
        return DataResponse.ok(productResponse);

    }

    @Override
    public DataResponse showProductHomePage() {
        Iterable<Category> categories = categoryRepository.findAll();
        List<ProductResHP> productResHPS = new ArrayList<>();
        List<ProductRes> productResList = new ArrayList<>();
        for(Category item: categories){
            // get list product
            List<Product> productList = productRepository.find10ProductByCategoryId(item.getCategoryId());
            for (Product p : productList) {
                ProductResHP productResHP = new ProductResHP();
                productResHP.setProductId(p.getProductId());
                productResHP.setProductName(p.getProductName());
                productResHP.setProductAvatar(p.getPhotosImagePath());
                productResHP.setProductPrice(p.getProductPrice());
                productResHPS.add(productResHP);
            }
            // init value for ProductRes
            ProductRes productRes = new ProductRes();
            productRes.setCategoryId(item.getCategoryId());
            productRes.setCategoryName(item.getCategoryName());
            productRes.setProductResHPS(productResHPS);

            productResList.add(productRes);
        }
        return DataResponse.ok(productResList);
    }

    @Override
    @Transactional
    public DataResponse changeStatusProducts(DeleteIDsRequest request) {
        logger.info("#changeStatusProducts");

        if (request.getList().isEmpty()) {
            return DataResponse.ok("");
        }

        List<Product> productChange = new ArrayList<>();

        for (Integer item:request.getList()) {

            if (!CommonUtils.isNumeric(String.valueOf(item))) {
                throw new BadRequestException(ErrorMessage.PRODUCT_ID_IS_INVALID);
            }

            Product product = productRepository.getProductByProductId(item);

            if (null == product) {
                throw new NotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
            }

            boolean isStatus = product.isDeleted();

            if (isStatus == Boolean.TRUE) {
                product.setDeleted(Boolean.FALSE);
            } else {
                product.setDeleted(Boolean.TRUE);
            }

            productChange.add(product);
        }

        Iterable<Product> entities =  productRepository.saveAll(productChange);

        return DataResponse.ok(entities);
    }

    private Specification<Product> buildFilterSpecification(FilterProductRequest filterProductRequest) {
        Specification<Product> spec = Specification.where(ProductCriteriaRepository.withIsDeleted(Boolean.FALSE));
        Category category;
        try {
            int idCategory = Integer.parseInt(String.valueOf(filterProductRequest.getIdCategory()));
            category = categoryRepository.findCategoryByCategoryIdAndDeleted(idCategory, Boolean.FALSE);
            spec = spec.and(withIsCategoryId(idCategory));
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorMessage.CATEGORY_ID_IS_INVALID);
        }

        if (null == category) {
            throw new BadRequestException(ErrorMessage.CATEGORY_IS_NOT_EXIST);
        }

        try {
            int optionPrice = Integer.parseInt(String.valueOf(filterProductRequest.getOptionPrice()));
            switch(optionPrice) {
                case 1:
                    spec = spec.and(withIsOptionPriceUnder500K());
                    break;
                case 2:
                    spec = spec.and(withIsOptionPriceF500kT1000k());
                    break;
                case 3:
                    spec = spec.and(withIsOptionPriceF1000kT1500k());
                    break;
                case 4:
                    spec = spec.and(withIsOptionPriceF1500kT2000k());
                    break;
                default:
                    spec = spec.and(withIsOptionPriceOver2000k());
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorMessage.OPTION_PRICE_IS_INVALID);
        }

        return spec;
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

        productDetailResponse.setProductId(product.getProductId());
        productDetailResponse.setProduct_name(product.getProductName());
        productDetailResponse.setProduct_price(product.getProductPrice());
        productDetailResponse.setProduct_quantity(product.getProductQuantity());
        productDetailResponse.setProduct_description(product.getProductDescription());
        productDetailResponse.setImages(product.getPhotosImagePath());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(List.of(product.getCategory().getCategoryId()));
        categoryDTO.setCategoryName(product.getCategory().getCategoryName());
        productDetailResponse.setCategoryDTO(categoryDTO);

        productDetailResponse.setProduct_discount(product.getDiscount());
        productDetailResponse.setDeleted(product.isDeleted());

        return productDetailResponse;
    }
}
