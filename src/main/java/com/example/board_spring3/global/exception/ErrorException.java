package com.example.board_spring3.global.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private final String message;
    private final int status;

    public ErrorException(ExceptionEnum exceptionEnum){
        this.message = exceptionEnum.getMessage();
        this.status = exceptionEnum.getStatus();
    }
}
