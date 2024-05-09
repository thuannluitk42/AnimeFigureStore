package com.tpsolution.animestore.payload;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String password;
    private String confirmPassword;
}
