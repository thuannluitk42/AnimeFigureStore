package com.tpsolution.animestore.constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode implements Serializable {
    SUCCESS("200", "Success"),
    ERROR_BAD_REQUEST("400", "Bad Request"),
    INTERNAL_SERVER_ERROR("500", "INTERNAL_SERVER_ERROR"),
    FORBIDDEN("403","FORBIDDEN"),
    UNAUTHORIZED("401", "Unauthorized");

    private String code;
    private String message;

}
