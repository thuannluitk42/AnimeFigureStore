package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.entity.Voucher;
import com.tpsolution.animestore.payload.DataResponse;

public interface VoucherServiceImp {
    DataResponse getRemainingUses(String username, String voucherCode);
    DataResponse useVoucher(String username, String voucherCode) throws Exception;
    DataResponse getAllVouchers();
    DataResponse getVoucherById(Long id);
    DataResponse createVoucher(Voucher voucher);
    DataResponse updateVoucher(Long id, Voucher voucherDetails);
    DataResponse deleteVoucher(Long id);
}
