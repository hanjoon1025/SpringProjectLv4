package com.example.board_spring3.global.dto;

import com.example.board_spring3.global.dto.InterfaceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class StatusResponseDto implements InterfaceDto {
    private String msg;
    private HttpStatus status;
}