package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.UserVoucher;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.entity.Voucher;
import com.tpsolution.animestore.payload.AddVoucherRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.repository.UserVoucherRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.repository.VoucherRepository;
import com.tpsolution.animestore.service.imp.VoucherServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VoucherService implements VoucherServiceImp {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private UserVoucherRepository userVoucherRepository;
    @Override
    public DataResponse getRemainingUses(String username, String voucherCode) {
        Users user = userRepository.findByUsername(username);
        Voucher voucher = voucherRepository.findByVoucherCode(voucherCode);
        UserVoucher userVoucher = userVoucherRepository.findByUserAndVoucher(user, voucher);

        if (userVoucher == null) {
            return DataResponse.ok(voucher.getMaxUsage());
        }

        return DataResponse.ok(voucher.getMaxUsage() - userVoucher.getUsageCount());
    }

    @Override
    @Transactional
    public DataResponse useVoucher(String username, String voucherCode) throws Exception {
        Users user = userRepository.findByUsername(username);
        Voucher voucher = voucherRepository.findByVoucherCode(voucherCode);
        UserVoucher userVoucher = userVoucherRepository.findByUserAndVoucher(user, voucher);

        if (userVoucher == null) {
            userVoucher = new UserVoucher();
            userVoucher.setUser(user);
            userVoucher.setVoucher(voucher);
            userVoucher.setUsageCount(0);
            userVoucher.setCreatedAt(LocalDateTime.now());
        }

        if (userVoucher.getUsageCount() >= voucher.getMaxUsage()) {
            throw new Exception("Voucher usage limit reached");
        }

        userVoucher.setUsageCount(userVoucher.getUsageCount() + 1);
        userVoucher.setLastUsed(LocalDateTime.now());
        userVoucher.setUpdatedAt(LocalDateTime.now());

        userVoucherRepository.save(userVoucher);

        // Update voucher usage count and check if it should be disabled
        voucher.setUsageCount(voucher.getUsageCount() + 1);
        if (voucher.getUsageCount() >= voucher.getMaxUsage()) {
            voucher.setActive(false);
        }
        return DataResponse.ok("Voucher used successfully");
    }

    @Override
    public DataResponse getAllVouchers() {
        return DataResponse.ok(voucherRepository.findAll());
    }

    @Override
    public DataResponse getVoucherById(Long id) {
        return DataResponse.ok(voucherRepository.findById(UUID.fromString(String.valueOf(id))));
    }

    @Override
    public DataResponse createVoucher(AddVoucherRequest request) {
        Voucher voucher = buildVoucher(request);
        LocalDateTime localDateTime = LocalDateTime.now();
        voucher.setCreatedAt(localDateTime);
        return DataResponse.ok(voucherRepository.save(voucher));
    }

    private Voucher buildVoucher(AddVoucherRequest request) {
        Voucher voucher = new Voucher();
        voucher.setVoucherCode(request.getVoucherCode());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setExpiryDate(request.getExpiryDate());
        voucher.setMaxUsage(request.getMaxUsage());
        voucher.setActive(request.isActive());
        return voucher;
    }

    @Override
    public DataResponse updateVoucher(Long id, AddVoucherRequest request) {
        return DataResponse.ok(voucherRepository.findById(UUID.fromString(String.valueOf(id))).map(voucher -> {
            voucher = buildVoucher(request);
            LocalDateTime localDateTime = LocalDateTime.now();
            voucher.setUpdatedAt(localDateTime);
            return voucherRepository.save(voucher);
        }));
    }

    @Override
    public DataResponse deleteVoucher(Long id) {
        voucherRepository.deleteById(UUID.fromString(String.valueOf(id)));
        return DataResponse.ok("");
    }
}
