package com.tpsolution.animestore.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {
    private int status = 200;
    private boolean isSuccess = true;
    private String desc;
    private Object data;
}
