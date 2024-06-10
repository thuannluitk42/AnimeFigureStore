package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Voucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoucherRepository extends CrudRepository<Voucher, UUID>,JpaSpecificationExecutor<Voucher> {

    Voucher findByVoucherCode(String voucherCode);
}
