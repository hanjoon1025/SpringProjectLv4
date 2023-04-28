package com.example.board_spring3.board.dto;

import com.example.board_spring3.board.entity.Board;
import com.example.board_spring3.comment.dto.CommentResponseDto;
import com.example.board_spring3.global.dto.InterfaceDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto implements InterfaceDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String username;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentResponseDtoList;

    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsers().getUsername();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
    public BoardResponseDto(Board board, List<CommentResponseDto> commentResponseDtoList){
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsers().getUsername();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.commentResponseDtoList = commentResponseDtoList;
    }
}
