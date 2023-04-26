package com.example.board_spring3.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long board_id;
    private String content;
}
