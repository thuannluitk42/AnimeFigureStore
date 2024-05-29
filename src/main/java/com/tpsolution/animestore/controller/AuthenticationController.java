package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.AuthRequest;
import com.tpsolution.animestore.payload.LoginRequest;
import com.tpsolution.animestore.payload.ResponseData;
import com.tpsolution.animestore.security.JwtUtilsHelper;
import com.tpsolution.animestore.service.imp.LoginServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/logout")
    public ResponseEntity<String> testLogoutRequest() {
        return ResponseEntity.ok("logout successful");
    }
    @GetMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("post request successful");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        ResponseData responseData = new ResponseData();
        if(loginServiceImp.checkLogin(loginRequest.getEmail(),loginRequest.getPassword())){
            String token = jwtUtilsHelper.generateToken(loginRequest.getEmail());
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
