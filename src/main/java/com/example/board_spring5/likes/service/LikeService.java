package com.example.board_spring5.likes.service;

import com.example.board_spring5.board.dto.BoardResponseDto;
import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.board.repository.BoardRepository;
import com.example.board_spring5.comment.dto.CommentResponseDto;
import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.comment.repository.CommentRepository;
import com.example.board_spring5.global.dto.StatusResponseDto;
import com.example.board_spring5.global.exception.ErrorException;
import com.example.board_spring5.global.exception.ExceptionEnum;
import com.example.board_spring5.likes.entity.Likes;
import com.example.board_spring5.likes.repository.LikeRepository;
import com.example.board_spring5.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ResponseEntity<?> updateBoardLike(Long id, Users users) {
        Board board = checkBoard(id);

        if (boardLike(users, board)) {
            board.undoLike();
            likeRepository.deleteByUsersIdAndBoardId(users.getId(), board.getId());
            StatusResponseDto statusResponseDto = new StatusResponseDto("게시글 좋아요를 취소하셨습니다.", HttpStatus.OK);
            return new ResponseEntity<>(new BoardResponseDto(board, statusResponseDto), HttpStatus.OK);
        } else {
            board.btnLike();
            Likes like = new Likes(users, board);
            likeRepository.save(like);
            StatusResponseDto statusResponseDto = new StatusResponseDto("게시글 좋아요를 누르셨습니다.", HttpStatus.OK);
            return new ResponseEntity<>(new BoardResponseDto(board, statusResponseDto), HttpStatus.OK);
        }
    }

    private Board checkBoard(Long id){
        return boardRepository.findById(id).orElseThrow(
                () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
        );
    }

    private Comment checkComment(Long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new ErrorException(ExceptionEnum.COMMENT_NOT_FOUND)
        );
    }

    private boolean boardLike(Users users, Board board){
        Optional<Likes> like = likeRepository.findByUsersIdAndBoardId(users.getId(),board.getId());
        if (like.isPresent()){
            return true;
        }
        return false;
    }

    private boolean commentLike(Users users, Board board, Comment comment){
        Optional<Likes> like = likeRepository.findByUsersIdAndBoardIdAndCommentId(users.getId(), board.getId(),comment.getId());
        if (like.isPresent()){
            return true;
        }
        return false;
    }

    public ResponseEntity<?> updateCommentLike(Long id, Users users) {
        Comment comment = checkComment(id);
        Board board = comment.getBoard();


        if (commentLike(users, board, comment)) {
            comment.undoLike();
            likeRepository.deleteByUsersIdAndBoardIdAndCommentId(users.getId(), board.getId(), comment.getId());
            StatusResponseDto statusResponseDto = new StatusResponseDto("댓글 좋아요를 취소하셨습니다.", HttpStatus.OK);
            return new ResponseEntity<>(new CommentResponseDto(comment, statusResponseDto), HttpStatus.OK);
        } else {
            comment.btnLike();
            Likes like = new Likes(users, board, comment);
            likeRepository.save(like);
            StatusResponseDto statusResponseDto = new StatusResponseDto("댓글 좋아요를 누르셨습니다.", HttpStatus.OK);
            return new ResponseEntity<>(new CommentResponseDto(comment, statusResponseDto), HttpStatus.OK);
        }
    }

}
