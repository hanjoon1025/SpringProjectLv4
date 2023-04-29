package com.example.board_spring3.global.exception;

import com.example.board_spring3.global.dto.InterfaceDto;
import lombok.Getter;

@Getter
public class ServiceException implements InterfaceDto{

    private final String message;
    private final int status;

    public ServiceException(ExceptionEnum exceptionEnum){
        this.message = exceptionEnum.getMessage();
        this.status = exceptionEnum.getStatus();
    }
}
