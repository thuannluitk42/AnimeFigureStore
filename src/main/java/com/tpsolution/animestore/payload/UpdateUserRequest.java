package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String address;
    private String phoneNumber;
    private String dob;
    private Set<Integer> roleId;
    private long userId;
    private String urlImage;
}
