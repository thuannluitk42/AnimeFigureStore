package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.AddVoucherRequest;
import com.tpsolution.animestore.payload.DataResponse;

public interface VoucherServiceImp {
    DataResponse getRemainingUses(String username, String voucherCode);
    DataResponse useVoucher(String username, String voucherCode) throws Exception;
    DataResponse getAllVouchers();
    DataResponse getVoucherById(Long id);
    DataResponse createVoucher(AddVoucherRequest request);
    DataResponse updateVoucher(Long id, AddVoucherRequest request);
    DataResponse deleteVoucher(Long id);
}
