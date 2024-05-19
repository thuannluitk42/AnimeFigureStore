package com.tpsolution.animestore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private int id;
    private String userName;
    private String password;
    private String fullName;
    private Date createdDate;
}
