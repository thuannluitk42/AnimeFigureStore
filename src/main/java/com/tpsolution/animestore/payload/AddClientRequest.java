package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class AddClientRequest {
    @NotEmpty()
    private String email;
    @NotEmpty()
    private String password;
    @NotEmpty()
    private Set<Integer> roleId;
}
