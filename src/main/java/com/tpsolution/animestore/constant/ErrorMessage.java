package com.tpsolution.animestore.constant;

import lombok.Data;


public enum ErrorMessage {

    USER_NOT_FOUND("USER_NOT_FOUND", "USER_NOT_FOUND"),
    EMAIL_IS_INVALID("EMAIL_IS_INVALID", "EMAIL_IS_INVALID"),
    USER_IS_EXISTED("USER_IS_EXISTED", "USER_IS_EXISTED"),
    PHONE_NUMBER_IS_INVALID("PHONE_NUMBER_IS_INVALID", "PHONE_NUMBER_IS_INVALID"),
    DAY_OF_BIRTH_IS_INVALID("DAY_OF_BIRTH_IS_INVALID", "DAY_OF_BIRTH_IS_INVALID"),
    OLD_PW_ERROR("OLD_PW_ERROR", "OLD_PW_NOT_RIGHT"),
    NEW_PW_DUPLICATED_OLD_PW("NEW_PW_DUPLICATED_OLD_PW", "NEW_PW_DUPLICATED_OLD_PW"),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND","TOKEN_NOT_FOUND"),
    COMPARE_PASSWORD_ERROR("COMPARE_PASSWORD_ERROR","COMPARE_PASSWORD_ERROR");

    private final String code;
    private final String message;
    ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
