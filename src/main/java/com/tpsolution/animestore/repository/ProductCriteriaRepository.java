package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCriteriaRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, Long> {

    static Specification<Product> withIsDeleted(Boolean isDeleted) {
        return (product, query, criteriaBuilder) -> criteriaBuilder.equal(product.get("isDelete"), isDeleted);
    }

    static Specification<Product> withProductSearch(String search) {
        final String lSearch = "%" + search.toLowerCase() + "%";
        return (product, query, criteriaBuilder)
                -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(product.get("product_name")), lSearch),
                criteriaBuilder.like(criteriaBuilder.lower(product.get("product_description")), lSearch),
                criteriaBuilder.like(criteriaBuilder.lower(product.get("discount")), lSearch));
    }

    static Specification<Product> withCreatedDate(String createdDate) {
        return (product, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(product.get("created_date")), createdDate);
    }
}
