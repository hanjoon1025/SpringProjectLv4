package com.example.board_spring5.comment.entity;

import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.comment.dto.CommentRequestDto;
import com.example.board_spring5.global.entity.Timestamped;
import com.example.board_spring5.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> recommentList = new ArrayList<>();

    private int likeCount = 0;

    public Comment (CommentRequestDto commentRequestDto){
        this.comment = commentRequestDto.getComment();
    }

    public void setBoard(Board board){
        this.board = board;
        board.getComment().add(this);
    }

       public void updateComment(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }

    public void setUsers(Users users){
        this.users = users;
    }

    public void undoLike() {
        if(likeCount -1 < 0) return;
        likeCount -= 1;
    }

    public void btnLike() {
//        if(likeCount -1 < 0) return;;
        likeCount += 1;
    }
}
