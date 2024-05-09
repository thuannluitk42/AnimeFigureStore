package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class UpdateUserRequest {
    @NotEmpty()
    private String fullName;
    @NotEmpty()
    private String email;
    @NotEmpty()
    private String address;
    @NotEmpty()
    private String phoneNumber;
    @NotEmpty()
    private String dob;
    @NotEmpty()
    private String password;
    @NotEmpty()
    private Set<Integer> roleId;
    @NotEmpty()
    private long userId;
    private String urlImage;
}
