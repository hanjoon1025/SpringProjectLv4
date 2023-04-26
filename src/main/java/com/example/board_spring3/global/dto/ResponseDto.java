package com.example.board_spring3.global.dto;

import lombok.Getter;

@Getter
public class ResponseDto {

    private String message;
    private int statusCode;

    public ResponseDto(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
}
