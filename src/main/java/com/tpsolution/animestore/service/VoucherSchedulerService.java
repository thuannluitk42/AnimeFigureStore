package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.Voucher;
import com.tpsolution.animestore.repository.VoucherRepository;
import com.tpsolution.animestore.service.imp.VoucherSchedulerServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoucherSchedulerService implements VoucherSchedulerServiceImp {

    @Autowired
    private VoucherRepository voucherRepository;

    // List of special days when vouchers should be activated or deactivated
    private static final List<LocalDate> SPECIAL_DAYS = List.of(
            LocalDate.of(LocalDate.now().getYear(), 1, 1),  // New Year's Day
            LocalDate.of(LocalDate.now().getYear(), 12, 25)  // Christmas Day
            // Add more special days as needed
    );

    @Scheduled(cron = "0 0 0 * * *")  // This cron expression means midnight every day
    @Transactional
    public void manageVouchersOnSpecialDays() {
        LocalDate today = LocalDate.now();

        List<Voucher> vouchers = (List<Voucher>) voucherRepository.findAll();

        for (Voucher voucher : vouchers) {
            if (SPECIAL_DAYS.contains(today)) {
                if (!voucher.isActive() && voucher.getExpiryDate().isAfter(today)) {
                    voucher.setActive(true);
                } else if (voucher.isActive() && (voucher.getUsageCount() >= voucher.getMaxUsage() || voucher.getExpiryDate().isBefore(today))) {
                    voucher.setActive(false);
                }
            } else if (voucher.getUsageCount() >= voucher.getMaxUsage() || voucher.getExpiryDate().isBefore(today)) {
                voucher.setActive(false);
            }
            voucherRepository.save(voucher);
        }
    }
}
