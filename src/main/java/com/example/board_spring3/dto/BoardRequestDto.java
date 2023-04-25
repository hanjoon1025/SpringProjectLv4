package com.example.board_spring3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String content;

}
