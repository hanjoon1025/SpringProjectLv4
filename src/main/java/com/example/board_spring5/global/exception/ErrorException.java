package com.example.board_spring5.global.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException{
    private final ExceptionEnum exceptionEnum;

    public ErrorException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    public int getStatus() {
        return this.exceptionEnum.getStatus();
    }
}
//@Getter
//public class ErrorException extends RuntimeException {
//
//    private final String message;
//    private final int status;
//
//    public ErrorException(ExceptionEnum exceptionEnum){
//        this.message = exceptionEnum.getMessage();
//        this.status = exceptionEnum.getStatus();
//    }
//}
