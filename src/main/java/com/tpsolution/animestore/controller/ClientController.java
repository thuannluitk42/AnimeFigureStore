package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.ResetPasswordRequest;
import com.tpsolution.animestore.service.UserService;
import com.tpsolution.animestore.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    UserServiceImp userService;

    /* chuc nang quen mat khau dung cho trang khach hang*/
    @GetMapping("/request-reset-password/{email}")
    public ResponseEntity<DataResponse> requestResetPW(@PathVariable String email) {
        return ResponseEntity.ok(userService.requestResetPW(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<DataResponse> resetPassword(@RequestBody ResetPasswordRequest userResetPassword) {
        return ResponseEntity.ok().body(userService.resetPassword(userResetPassword.getToken(),
                userResetPassword.getPassword(), userResetPassword.getConfirmPassword()));
    }
}
