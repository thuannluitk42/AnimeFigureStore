package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePWIdRequest {
    @NotEmpty()
    private String userId;
    @NotEmpty()
    private String newPassword;
}
