package com.example.board_spring5.comment.dto;

import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.global.dto.InterfaceDto;
import com.example.board_spring5.global.dto.StatusResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto implements InterfaceDto {

    private Long id;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer likeCount;
    private StatusResponseDto statusResponseDto;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.username = comment.getUsers().getUsername();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikeCount();

    }
    public CommentResponseDto(Comment comment,StatusResponseDto statusResponseDto){
        this.id = comment.getId();
        this.username = comment.getUsers().getUsername();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikeCount();
        this.statusResponseDto = statusResponseDto;
    }
}

