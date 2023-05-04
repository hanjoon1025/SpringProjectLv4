package com.example.board_spring5.board.dto;

import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.comment.dto.CommentResponseDto;
import com.example.board_spring5.global.dto.InterfaceDto;
import com.example.board_spring5.global.dto.StatusResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter // generates getters for the fields in the class automatically
public class BoardResponseDto implements InterfaceDto { // implements InterfaceDto which means that this class much implement all the methods defined in the InterfaceDto
// Client에게 반환될 때 전달되는 Data
    private final Long id;
    private final String title;
    private final String content;
    private final String username;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Integer likeCount; // 좋아요 수를 저장할 필드
    private List<CommentResponseDto> commentList; // 댓글도 추가적으로 반환해줘야 하기떄문에 CommentList 추가
    private StatusResponseDto statusResponseDto;
    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsers().getUsername(); // User Entity에 있는 username - 게시글 작성자의 username으로 설정
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.likeCount = board.getLikeCount();

    }

    public BoardResponseDto(Board board, StatusResponseDto statusResponseDto){
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsers().getUsername(); // User Entity에 있는 username - 게시글 작성자의 username으로 설정
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.likeCount = board.getLikeCount();
        this.statusResponseDto = statusResponseDto;
    }
    public BoardResponseDto(Board board, List<CommentResponseDto> commentList){
    // Board Entity의 데이터와 해당 Board의 게시물과 관련된 댓글 목록 포함
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsers().getUsername(); // User Entity에 있는 username - 게시글 작성자의 username으로 설정
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.likeCount = board.getLikeCount();
        this.commentList = commentList;
    }


}
