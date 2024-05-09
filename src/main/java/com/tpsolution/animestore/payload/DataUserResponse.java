package com.tpsolution.animestore.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataUserResponse {
    private int userId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String dob;
    private String avatar;
    private int roleId;
}
