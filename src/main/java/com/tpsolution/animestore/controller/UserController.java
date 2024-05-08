package com.tpsolution.animestore.controller;


import com.tpsolution.animestore.payload.AddUserRequest;
import com.tpsolution.animestore.payload.ChangePWRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.UpdateUserRequest;
import com.tpsolution.animestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("GET request successful");
    }

    @PostMapping("/add-new-user")
    public ResponseEntity<DataResponse> insertNewUser(@RequestBody AddUserRequest request) {
        return ResponseEntity.ok(userService.insertNewUser(request));
    }

    @PostMapping("/update-info-user")
    public ResponseEntity<DataResponse> updateInfoUser(@RequestBody UpdateUserRequest request, @RequestParam("avatar") MultipartFile multipartFile) {

        if (!multipartFile.isEmpty()) {
            return ResponseEntity.ok(userService.updateUser(request));
        } else {
            request.setImageName(null);
            request.setUrlImage(null);
            return ResponseEntity.ok(userService.updateUser(request));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<DataResponse> changePassword(@RequestBody ChangePWRequest request) {
        return ResponseEntity.ok(userService.changePW(request));
    }

    @PostMapping("/request-reset-password/{email}")
    public ResponseEntity<DataResponse> resetRequestPW(@PathVariable String email) {
        return ResponseEntity.ok(userService.resetRequestPW(email));
    }

//    @PostMapping("/reset-password")
//    public ResponseEntity<DataResponse> resetPassword(@RequestBody ResetPWRequest resetPW) {
//        return ResponseEntity.ok().body(userService.resetPassword(userResetPassword.getToken(), userResetPassword.getPassword(), userResetPassword.getConfirmPassword()));
//    }

}
