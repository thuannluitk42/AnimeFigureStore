package com.tpsolution.animestore.enums;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_ADMIN("admin"), ROLE_CLIENT("client");

    private String description;

    Role(String description) {
        this.description = description;
    }
}
