package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryCriteriaRepository extends JpaSpecificationExecutor<Category>, JpaRepository<Category, Long> {
    static Specification<Category> withIsDeleted(Boolean isDeleted) {
        return (category, query, criteriaBuilder) -> criteriaBuilder.equal(category.get("is_deleted"), isDeleted);
    }

    static Specification<Category> withCategoryNameSearch(String search) {
        final String lSearch = "%" + search.toLowerCase() + "%";
        return (category, query, criteriaBuilder)
                -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(category.get("category_name")), lSearch));
    }
}
