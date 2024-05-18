package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.service.UserService;
import com.tpsolution.animestore.service.imp.UserServiceImp;
import com.tpsolution.animestore.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.tpsolution.animestore.utils.CommonUtils.randomIdentifier;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    UserServiceImp userService;

    @GetMapping("/get-info-user/{userId}")
    public ResponseEntity<DataResponse> getInfoDetailUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.getInfoDetailUser(userId));
    }

    @PostMapping("/add-new-user")
    public ResponseEntity<DataResponse> insertNewUser(@RequestBody AddUserRequest request) {
        return ResponseEntity.ok(userService.insertNewUser(request));
    }

    @PostMapping("/add-500-user")
    public ResponseEntity<DataResponse> insert500NewUser() {
        Set<Integer> roleId = new HashSet<>();
        roleId.add(6);
        for (int i = 0 ; i < 20 ; i++) {
            AddUserRequest ar = new AddUserRequest();
            ar.setEmail(randomIdentifier()+i+"@yopmail.com");
            ar.setPassword("Abc12345@");
            ar.setRoleId(roleId);
            userService.insertNewUser(ar);
        }
        return ResponseEntity.ok(DataResponse.ok(null));
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

    @GetMapping("/search-user")
    public ResponseEntity<DataResponse> getUserAll(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(userService.getUserAll(searchRequest));
    }

    @GetMapping("/find-all-user")
    public ResponseEntity<DataResponse> findAllUser() {
        return ResponseEntity.ok(userService.findAllUser());
    }

    @PostMapping("/change-status-user")
    public ResponseEntity<DataResponse> changeStatusUser(@RequestBody DeleteIDsRequest request) {
        return ResponseEntity.ok(userService.changeStatusUser(request));
    }

}
