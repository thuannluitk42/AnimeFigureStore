package com.tpsolution.animestore.exception;

import com.tpsolution.animestore.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        String errorCode = e.getErrorCode();
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(errorCode, message));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        String errorCode = e.getErrorCode();
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(errorCode, message));
    }

}
