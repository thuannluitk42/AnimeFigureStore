package com.tpsolution.animestore.constant;


public enum ErrorMessage {

    USER_NOT_FOUND("USER_NOT_FOUND", "USER_NOT_FOUND"),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "PRODUCT_NOT_FOUND"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "ORDER_NOT_FOUND"),
    EMAIL_IS_INVALID("EMAIL_IS_INVALID", "EMAIL_IS_INVALID"),
    PRODUCT_ID_IS_INVALID("PRODUCT_ID_IS_INVALID", "PRODUCT_ID_IS_INVALID"),
    ORDER_ID_IS_INVALID("ORDER_ID_IS_INVALID", "ORDER_ID_IS_INVALID"),
    USER_ID_IS_INVALID("USER_ID_IS_INVALID", "USER_ID_IS_INVALID"),
    TOTAL_ORDER_IS_INVALID("TOTAL_ORDER_IS_INVALID", "TOTAL_ORDER_IS_INVALID"),
    PRODUCT_NAME_IS_INVALID("PRODUCT_NAME_IS_INVALID", "PRODUCT_NAME_IS_INVALID"),
    USER_ID_IS_NOT_NULL("USER_ID_IS_NOT_NULL", "USER_ID_IS_NOT_NULL"),
    ORDER_ID_IS_NOT_NULL("ORDER_ID_IS_NOT_NULL", "ORDER_ID_IS_NOT_NULL"),
    PRODUCT_NAME_IS_NOT_NULL("PRODUCT_NAME_IS_NOT_NULL", "PRODUCT_NAME_IS_NOT_NULL"),
    PRODUCT_PRICE_IS_INVALID("PRODUCT_PRICE_IS_INVALID", "PRODUCT_PRICE_IS_INVALID"),
    PRODUCT_QUANTITY_IS_INVALID("PRODUCT_QUANTITY_IS_INVALID", "PRODUCT_QUANTITY_IS_INVALID"),
    PRODUCT_IMAGES_IS_INVALID("PRODUCT_IMAGES_IS_INVALID", "PRODUCT_IMAGES_IS_INVALID"),
    PRODUCT_IMAGES_EXTEND_INVALID("PRODUCT_IMAGES_MUST_JPG,JPEG,PNG,GIF,BMP", "PRODUCT_IMAGES_MUST_JPG,JPEG,PNG,GIF,BMP"),
    USER_IS_EXISTED("USER_IS_EXISTED", "USER_IS_EXISTED"),
    PHONE_NUMBER_IS_INVALID("PHONE_NUMBER_IS_INVALID", "PHONE_NUMBER_IS_INVALID"),
    DAY_OF_BIRTH_IS_INVALID("DAY_OF_BIRTH_IS_INVALID", "DAY_OF_BIRTH_IS_INVALID"),
    OLD_PW_ERROR("OLD_PW_ERROR", "OLD_PW_NOT_RIGHT"),
    NEW_PW_DUPLICATED_OLD_PW("NEW_PW_DUPLICATED_OLD_PW", "NEW_PW_DUPLICATED_OLD_PW"),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND","TOKEN_NOT_FOUND"),
    COMPARE_PASSWORD_ERROR("COMPARE_PASSWORD_ERROR","COMPARE_PASSWORD_ERROR"),
    SEARCH_REQUEST_IS_NOT_NULL("SEARCH_REQUEST_IS_NOT_NULL", "SEARCH_REQUEST_IS_NOT_NULL"),
    CATEGORY_NAME_IS_INVALID("CATEGORY_NAME_IS_INVALID", "CATEGORY_NAME_IS_INVALID"),
    CATEGORY_ID_IS_INVALID("CATEGORY_ID_IS_INVALID", "CATEGORY_ID_IS_INVALID"),
    CATEGORY_REQUEST_IS_NOT_NULL("CATEGORY_REQUEST_IS_NOT_NULL", "CATEGORY_REQUEST_IS_NOT_NULL"),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", "CATEGORY_NOT_FOUND"),
    CATEGORY_IS_NOT_EXIST("CATEGORY_IS_NOT_EXIST", "CATEGORY_IS_NOT_EXIST");

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
