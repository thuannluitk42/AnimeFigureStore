package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.service.UserService;
import com.tpsolution.animestore.utils.FileUploadUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping(value = "/update-info-user", consumes = { "multipart/form-data" })
    public ResponseEntity<DataResponse> updateInfoUser(@RequestPart("data") UpdateUserRequest request,
                                                       @RequestParam("avatar") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            request.setUrlImage(fileName);
            String uploadDir = "user-photos/" + request.getUserId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            return ResponseEntity.ok(userService.updateUser(request));
        } else {

            return ResponseEntity.ok(userService.updateUser(request));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<DataResponse> changePassword(@RequestBody ChangePWRequest request) {
        return ResponseEntity.ok(userService.changePW(request));
    }

    @PostMapping("/request-reset-password/{email}")
    public ResponseEntity<DataResponse> requestResetPW(@PathVariable String email) {
        return ResponseEntity.ok(userService.requestResetPW(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<DataResponse> resetPassword(@RequestBody ResetPasswordRequest userResetPassword) {
        return ResponseEntity.ok().body(userService.resetPassword(userResetPassword.getToken(), userResetPassword.getPassword(), userResetPassword.getConfirmPassword()));
    }


}
