package com.tpsolution.animestore.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tpsolution.animestore.dto.RolesDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    List<RolesDTO> roleList = new ArrayList<RolesDTO>();
    //private String password;
}
