package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.Set;

@Data
public class SignUpRequest {
    private String fullName;
    private String email;
    private String password;
    private Set<Integer> roleId;
}
