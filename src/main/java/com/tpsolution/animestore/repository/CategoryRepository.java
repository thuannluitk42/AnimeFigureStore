package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

    @Query("SELECT u from Category u  where (lower(u.categoryName) like lower(:categoryName)) and u.isDeleted =:isDelete")
    List<Category> findCategoriesByCategoryNameAndIsDelete(String categoryName, Boolean isDelete);

    @Query("SELECT u from Category u  where u.categoryId =:categoryId and u.isDeleted =:isDelete")
    Category findCategoryByCategoryIdAndDeleted(int categoryId, Boolean isDelete);
}
