package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePWRequest {
    @NotEmpty()
    private String email;

    @NotEmpty()
    private String oldPassword;

    @NotEmpty()
    private String newPassword;
}
