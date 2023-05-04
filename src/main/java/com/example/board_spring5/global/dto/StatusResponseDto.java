package com.example.board_spring5.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class StatusResponseDto implements InterfaceDto {
    private String message;
    private int statusCode;


    public StatusResponseDto(String message, HttpStatus httpStatus) {
        this.message = message;
        this.statusCode = httpStatus.value();
    }

}