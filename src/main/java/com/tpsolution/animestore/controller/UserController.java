package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.service.UserService;
import com.tpsolution.animestore.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    // class variable
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    final java.util.Random rand = new java.util.Random();
    // consider using a Map<String,Boolean> to say whether the identifier is being used or not
    final Set<String> identifiers = new HashSet<String>();

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    @Autowired
    UserService userService;

    @GetMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("GET request successful");
    }

    /*Xem chi tiet thong tin 1 user*/
    @GetMapping("/get-info-user/{userId}")
    public ResponseEntity<DataResponse> getInfoDetailUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.getInfoDetailUser(userId));
    }

    /*Them moi 1 user*/
    @PostMapping("/add-new-user")
    public ResponseEntity<DataResponse> insertNewUser(@RequestBody AddUserRequest request) {
        return ResponseEntity.ok(userService.insertNewUser(request));
    }

    @PostMapping("/add-500-user")
    public ResponseEntity<DataResponse> insert500NewUser() {
        Set<Integer> roleId = new HashSet<>();
        roleId.add(6);
        for (int i = 0 ; i < 500 ; i++) {
            AddUserRequest ar = new AddUserRequest();
            ar.setEmail(randomIdentifier()+i+"@yopmail.com");
            ar.setPassword("Abc12345@");
            ar.setRoleId(roleId);
            userService.insertNewUser(ar);
        }
        return ResponseEntity.ok(DataResponse.ok(null));
    }

    /*Cap nhat thong tin 1 user*/
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

    /*Thuc hien thay doi mat khau cho user trong trang admin*/
    @PostMapping("/change-password")
    public ResponseEntity<DataResponse> changePassword(@RequestBody ChangePWRequest request) {
        return ResponseEntity.ok(userService.changePW(request));
    }

    @GetMapping("/paging")
    public ResponseEntity<DataResponse> getContractAll(UsersRequest usersRequest) {
        return ResponseEntity.ok(userService.getUserAll(usersRequest));
    }
}
