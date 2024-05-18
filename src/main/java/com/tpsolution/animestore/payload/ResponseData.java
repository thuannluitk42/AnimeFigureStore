package com.tpsolution.animestore.payload;

import lombok.Data;

@Data
public class ResponseData {
    private int status = 200;
    private boolean isSuccess = true;
    private String desc;
    private Object data;
}
