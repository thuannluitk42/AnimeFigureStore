package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.AddCategoryRequest;
import com.tpsolution.animestore.payload.AddVoucherRequest;
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

    @PostMapping("/create-voucher")
    public ResponseEntity<DataResponse> createVoucher(@RequestBody AddVoucherRequest request) {
        return ResponseEntity.ok(voucherService.createVoucher(request));
    }

    @GetMapping("/voucher/{id}")
    public ResponseEntity<DataResponse> getVoucher(@PathVariable Long id) {
        return ResponseEntity.ok().body(voucherService.getVoucherById(id));
    }

    @GetMapping("/voucher")
    public ResponseEntity<DataResponse> getListVoucher() {
        return ResponseEntity.ok().body(voucherService.getAllVouchers());
    }

    @PutMapping("/update-voucher/{id}")
    public ResponseEntity<DataResponse> updateVoucher(@PathVariable Long id, @RequestBody AddVoucherRequest request) {
        return ResponseEntity.ok(voucherService.updateVoucher(id, request));
    }

    @DeleteMapping("/delete-voucher/{id}")
    public ResponseEntity<DataResponse> deleteVoucher(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.deleteVoucher(id));
    }
}
