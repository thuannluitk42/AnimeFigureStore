package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.ResponseData;
import com.tpsolution.animestore.service.imp.LoginServiceImp;
import com.tpsolution.animestore.utils.JwtUtilsHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    LoginServiceImp loginServiceImp;

    @Autowired
    JwtUtilsHelper jwtUtilsHelper;

    @GetMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("GET request successful");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String username, @RequestParam String password){
        ResponseData responseData = new ResponseData();
        // chay lan dau tien thi lay ma nay replace vao jwt.privateKey
//        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String encrypted = Encoders.BASE64.encode(secretKey.getEncoded());
//        System.out.println(encrypted);
        if(loginServiceImp.checkLogin(username,password)){
            String token = jwtUtilsHelper.generateToken(username);
            responseData.setData(token);
        }else{
            responseData.setData("");
            responseData.setSuccess(false);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        return loginServiceImp.logout(request);
    }

}
