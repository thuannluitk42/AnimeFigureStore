package com.tpsolution.animestore.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailResponse {
    private int userId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String dob;
    private String avatar;
    private boolean isDeleted;
    private int roleId;
    private String roleName;
    private String password;
}
