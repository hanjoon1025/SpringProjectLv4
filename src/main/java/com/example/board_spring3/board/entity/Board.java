package com.example.board_spring3.board.entity;

import com.example.board_spring3.board.dto.BoardRequestDto;
import com.example.board_spring3.comment.entity.Comment;
import com.example.board_spring3.global.entity.Timestamped;
import com.example.board_spring3.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME")
    private Users users;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comment = new ArrayList<>();

    public Board(BoardRequestDto boardRequestDto, Users users){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.users = users;
    }

    public void update(BoardRequestDto boardRequestDto){
        this.content = boardRequestDto.getContent();
        this.title = boardRequestDto.getTitle();
    }
}
