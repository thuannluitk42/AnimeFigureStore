package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.DataResponse;

public interface VoucherServiceImp {
    DataResponse getRemainingUses(String username, String voucherCode);
    DataResponse useVoucher(String username, String voucherCode) throws Exception;
}
