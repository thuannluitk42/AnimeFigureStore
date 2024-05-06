package com.tpsolution.animestore.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SignUpRequest {
    private String fullName;
    private String email;
    private String password;
    private Set<Integer> roleId;
}
