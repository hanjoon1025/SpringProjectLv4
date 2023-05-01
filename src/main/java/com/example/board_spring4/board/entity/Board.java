package com.example.board_spring4.board.entity;

import com.example.board_spring4.board.dto.BoardRequestDto;
import com.example.board_spring4.comment.entity.Comment;
import com.example.board_spring4.global.entity.Timestamped;
import com.example.board_spring4.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // indicating that this is Entity Class
@Getter // generates getter methods for all fields in the clas
@Setter // generates setter methods for all fields in the class
@NoArgsConstructor //generates a no-argument constructor for the class
public class Board extends Timestamped { // Timestamped 상속 (createdAt, ModifiedAt)

    @Id //indicating that this is the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // with .IDENTITY, database will generate a unique value for id
    private Long id;

    @Column // becomes a column in the data table
    private String title;

    @Column // becomes a column in the data table
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // Board - Many, User - One / FetchType.LAZY: loaded when needed
    @JoinColumn(name = "USERNAME") //Board와 Users 테이블 조인 - USERNAME 열을 사용하여 Users 테이블의 primary key와 연결
    private Users users;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    // Board - One, Comment - Many / FetchType.EAGER: loaded all at once / CascadeType.ALL: 게시글이 삭제되면 연결된 댓글도 삭제 / orphanRemoval=true: 게시글의 댓글 목록에서 comment가 삭제되면 db에서도 삭제
    private List<Comment> comment = new ArrayList<>(); // initializing to an empty list is MUST. 알아야할게 아니라 일단 외워야함.

    public Board(BoardRequestDto boardRequestDto, Users users){ // boardRequestDto와 users 값을 기반으로 필드 설정
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.users = users;
    }

    public void update(BoardRequestDto boardRequestDto){ // update 메소드 - boardRequestDto 값을 기반으로 제목 및 내용 업데이트
        this.content = boardRequestDto.getContent();
        this.title = boardRequestDto.getTitle();
    }
}
