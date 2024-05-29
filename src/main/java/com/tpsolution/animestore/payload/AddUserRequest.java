package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.Set;

@Data
public class AddUserRequest {
    private String email;
    private String password;
    private Set<Integer> roleId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String dob;
    private String urlImage;
}
