package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDetailResponse {
    private int userId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String dob;
    private String avatar;
}
