package com.example.board_spring3.global.exception;

import com.example.board_spring3.global.dto.InterfaceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto implements InterfaceDto {

    private String message;
    private int status;

    public ErrorResponseDto(ExceptionEnum exceptionEnum){
        this.message = exceptionEnum.getMessage();
        this.status = exceptionEnum.getStatus();
    }
}