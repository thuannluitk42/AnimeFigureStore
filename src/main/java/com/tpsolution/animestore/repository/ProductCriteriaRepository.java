package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.constant.StringConstant;
import com.tpsolution.animestore.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCriteriaRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, Long> {

    static Specification<Product> withIsDeleted(Boolean isDeleted) {
        return (product, query, criteriaBuilder) -> criteriaBuilder.equal(product.get("isDeleted"), isDeleted);
    }

    static Specification<Product> withIsCategoryId(int categoryId) {
        return (product, query, criteriaBuilder) -> criteriaBuilder.equal(product.get("category").get("categoryId"), categoryId);
    }

    static Specification<Product> withIsOptionPriceUnder500K() {
        return (product, query, criteriaBuilder) -> criteriaBuilder.lessThan(product.get("productPrice"), StringConstant.FilterBy._500K);
    }
    static Specification<Product> withIsOptionPriceF500kT1000k() {
        return (product, query, criteriaBuilder) -> criteriaBuilder.between(product.get("productPrice"), StringConstant.FilterBy._500K, StringConstant.FilterBy._1000K);
    }
    static Specification<Product> withIsOptionPriceF1000kT1500k() {
        return (product, query, criteriaBuilder) -> criteriaBuilder.between(product.get("productPrice"), StringConstant.FilterBy._1000K,StringConstant.FilterBy._1500K);
    }
    static Specification<Product> withIsOptionPriceF1500kT2000k() {
        return (product, query, criteriaBuilder) -> criteriaBuilder.between(product.get("productPrice"), StringConstant.FilterBy._1500K, StringConstant.FilterBy._2000K);
    }
    static Specification<Product> withIsOptionPriceOver2000k() {
        return (product, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(product.get("productPrice"), StringConstant.FilterBy._2000K);
    }
    static Specification<Product> withIsSortByPriceASC() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("productPrice")));
            return criteriaBuilder.conjunction();
        };
    }
    static Specification<Product> withIsSortByPriceDESC() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("productPrice")));
            return criteriaBuilder.conjunction();
        };
    }
    static Specification<Product> withIsSortByProductNameASC() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("productName")));
            return criteriaBuilder.conjunction();
        };
    }
    static Specification<Product> withIsSortByProductNameDESC() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("productName")));
            return criteriaBuilder.conjunction();
        };
    }
    static Specification<Product> withIsSortByProductOLDEST() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("createdDate")));
            return criteriaBuilder.conjunction();
        };
    }
    static Specification<Product> withIsSortByProductNEWEST() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
            return criteriaBuilder.conjunction();
        };
    }
//    static Specification<Product> withIsSortByProductHotSales() {
//        return (product, query, criteriaBuilder) -> query.orderBy(criteriaBuilder.asc(product.get("createdDate")));
//    }
//    static Specification<Product> withIsSortByProductQuantitiesDESC() {
//        return (product, query, criteriaBuilder) ->  query.orderBy(criteriaBuilder.desc(product.get("createdDate")));
//    }
    static Specification<Product> withProductSearch(String search) {
        final String lSearch = "%" + search.toLowerCase() + "%";
        return (product, query, criteriaBuilder)
                -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(product.get("productName")), lSearch),
                criteriaBuilder.like(criteriaBuilder.lower(product.get("productDescription")), lSearch),
                criteriaBuilder.like(criteriaBuilder.lower(product.get("discount")), lSearch));
    }

    static Specification<Product> withCreatedDate(String createdDate) {
        return (product, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(product.get("createdDate")), createdDate);
    }
}
