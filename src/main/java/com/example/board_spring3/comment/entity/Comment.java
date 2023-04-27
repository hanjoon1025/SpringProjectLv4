package com.example.board_spring3.comment.entity;

import com.example.board_spring3.comment.dto.CommentRequestDto;
import com.example.board_spring3.board.entity.Board;
import com.example.board_spring3.comment.dto.CommentResponseDto;
import com.example.board_spring3.global.entity.Timestamped;
import com.example.board_spring3.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    public Comment (Users users, Board board, CommentRequestDto commentRequestDto){
        this.users = users;
        this.board = board;
        this.content = commentRequestDto.getContent();
    }
       public void updateContent(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
