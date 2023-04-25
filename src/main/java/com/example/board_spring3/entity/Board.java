package com.example.board_spring3.entity;

import com.example.board_spring3.dto.board.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long userId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String username;

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
}
