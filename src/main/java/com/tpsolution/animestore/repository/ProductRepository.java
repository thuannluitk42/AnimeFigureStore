package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Category;
import com.tpsolution.animestore.entity.Product;
import com.tpsolution.animestore.entity.Users;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Query("SELECT u from Product u  where (lower(u.productName) like lower(:productName)) and u.isDeleted =:isDelete")
    List<Product> findProductByProductNameAndDeleted(String productName, Boolean isDelete);

    Product findProductByProductIdAndDeleted(int productId, Boolean isDelete);
}
