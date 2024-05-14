package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, UUID>, JpaSpecificationExecutor<OrderDetail> {
}
