package com.tpsolution.animestore.controller;


import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.service.imp.RoleServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    RoleServiceImp roleService;

    @GetMapping("/get-all-role")
    public ResponseEntity<DataResponse> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRole());
    }
}
