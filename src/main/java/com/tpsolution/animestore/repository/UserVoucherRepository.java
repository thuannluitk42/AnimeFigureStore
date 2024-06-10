package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.UserVoucher;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.entity.Voucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserVoucherRepository extends CrudRepository<UserVoucher, UUID>, JpaSpecificationExecutor<UserVoucher> {
    UserVoucher findByUserAndVoucher(Users user, Voucher voucher);
}
