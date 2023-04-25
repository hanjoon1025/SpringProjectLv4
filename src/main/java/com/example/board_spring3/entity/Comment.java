package com.example.board_spring3.entity;

import com.example.board_spring3.dto.comment.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"board", "user"}) //실제로는 코멘트에 반영되는 property가 아니므로 표기
public class Comment extends Timestamped{

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

    public Comment (CommentRequestDto commentRequestDto, Users users, Board board){
        this.content = commentRequestDto.getContent();
        setUsers(users);
        setBoard(board);
    }

    public Comment (Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.users = comment.getUsers();
        this.board = comment.getBoard();
    }

    private void setUsers(Users users) {
        this.users = users;
    }

    private void setBoard(Board board) {
        this.board = board;
    }

    public void update(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
    }

}
