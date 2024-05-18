package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.AuthRequest;
import com.tpsolution.animestore.payload.ResponseData;
import com.tpsolution.animestore.security.JwtUtilsHelper;
import com.tpsolution.animestore.service.imp.LoginServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    LoginServiceImp loginServiceImp;

    @Autowired
    JwtUtilsHelper jwtUtilsHelper;
    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout() {
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok().build();
//    }

//    @GetMapping("/login")
//    public ResponseEntity<String> showLoginForm() {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
//            return ResponseEntity.ok("login");
//        }
//
//        return ResponseEntity.ok("redirect:///");
//    }

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
        } else {
            responseData.setData("");
            responseData.setSuccess(false);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtUtilsHelper.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        return loginServiceImp.logout(request);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('client')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("admin/adminProfile")
    @PreAuthorize("hasAuthority('admin')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

}
