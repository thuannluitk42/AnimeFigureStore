package com.tpsolution.animestore.exception;

import com.tpsolution.animestore.constant.ErrorMessage;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private String errorCode;
    private String message;
    public NotFoundException(ErrorMessage errorMessage) {
        this.errorCode = errorMessage.getCode();
        this.message = errorMessage.getMessage();
    }

    public NotFoundException() {
        super();
    }

    public NotFoundException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
