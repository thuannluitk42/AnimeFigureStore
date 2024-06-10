package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping("/{username}/{voucherCode}/remaining-uses")
    public ResponseEntity<DataResponse> getRemainingUses(@PathVariable String username, @PathVariable String voucherCode) {
        return ResponseEntity.ok().body(voucherService.getRemainingUses(username, voucherCode));
    }

    @PostMapping("/{username}/{voucherCode}/use")
    public ResponseEntity<DataResponse> useVoucher(@PathVariable String username, @PathVariable String voucherCode) {
        try {
            return ResponseEntity.ok().body(voucherService.useVoucher(username, voucherCode));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DataResponse.ok(e.getMessage()));
        }
    }
}
