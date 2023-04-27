package com.example.board_spring3.board.entity;

import com.example.board_spring3.board.dto.BoardRequestDto;
import com.example.board_spring3.comment.entity.Comment;
import com.example.board_spring3.global.entity.Timestamped;
import com.example.board_spring3.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column
    private Long userId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String username;

    @OneToMany
    @OrderBy
    private List<Comment> commentList;

    public Board(BoardRequestDto boardRequestDto, Long userId, String username){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.userId = userId;
        this.username = username;
    }

    public void update(BoardRequestDto boardRequestDto){
        this.content = boardRequestDto.getContent();
        this.title = boardRequestDto.getTitle();
    }

    public void addComment(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
