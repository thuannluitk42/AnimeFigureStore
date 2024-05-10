package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Users;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCriteriaRepository extends JpaSpecificationExecutor<Users>, JpaRepository<Users, Long> {
    static Specification<Users> withIsDeleted(Boolean isDeleted) {
        return (user, query, criteriaBuilder) -> criteriaBuilder.equal(user.get("isDelete"), isDeleted);
    }

    static Specification<Users> withClientSearch(String search) {
        final String lSearch = "%" + search.toLowerCase() + "%";
        return (user, query, criteriaBuilder)
                           -> criteriaBuilder.or(
                           criteriaBuilder.like(criteriaBuilder.lower(user.get("email")), lSearch),
                           criteriaBuilder.like(criteriaBuilder.lower(user.get("fullname")), lSearch));
    }

    static Specification<Users> withCreatedDate(String createdDate) {
        return (user, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(user.get("is_deleted")), createdDate);
    }
}
