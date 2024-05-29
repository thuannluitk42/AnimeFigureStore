package com.tpsolution.animestore.exception;

import com.tpsolution.animestore.constant.ErrorMessage;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private String errorCode;
    private String message;

    public BadRequestException(ErrorMessage errorMessage) {
        this.errorCode = errorMessage.getCode();
        this.message = errorMessage.getMessage();
    }
}
