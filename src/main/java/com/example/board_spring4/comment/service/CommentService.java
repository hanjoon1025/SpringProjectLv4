package com.example.board_spring4.comment.service;

import com.example.board_spring4.board.entity.Board;
import com.example.board_spring4.board.repository.BoardRepository;
import com.example.board_spring4.comment.dto.CommentRequestDto;
import com.example.board_spring4.comment.dto.CommentResponseDto;
import com.example.board_spring4.comment.entity.Comment;
import com.example.board_spring4.comment.repository.CommentRepository;
import com.example.board_spring4.global.dto.InterfaceDto;
import com.example.board_spring4.global.dto.StatusResponseDto;
import com.example.board_spring4.global.exception.ErrorException;
import com.example.board_spring4.global.exception.ErrorResponseDto;
import com.example.board_spring4.global.exception.ExceptionEnum;
import com.example.board_spring4.global.jwt.JwtUtil;
import com.example.board_spring4.user.entity.UserRoleEnum;
import com.example.board_spring4.user.entity.Users;
import com.example.board_spring4.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public InterfaceDto createComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);

        Board board = boardRepository.findById(commentRequestDto.getBoard_id()).orElseThrow(
                () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
        );

        Users users = getUserByToken(token);

        if (users != null) {
            Comment comment = new Comment(commentRequestDto);

            comment.setBoard(board);
            comment.setUsers(users);

            commentRepository.save(comment);

            return new CommentResponseDto(comment);
        } else {
            return new ErrorResponseDto(ExceptionEnum.TOKEN_NOT_FOUND);
        }
    }

    @Transactional
    public InterfaceDto updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);

        Users users = getUserByToken(token);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ErrorException(ExceptionEnum.COMMENT_NOT_FOUND)
        );

        if (comment.getUsers().getUsername().equals(users.getUsername()) || users.getRole() == UserRoleEnum.ADMIN) {
            comment.updateComment(commentRequestDto);

            return new CommentResponseDto(comment);
        } else {
            return new ErrorResponseDto(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
        }
    }

    @Transactional
    public InterfaceDto deleteComment(Long id, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Users users = getUserByToken(token);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ErrorException(ExceptionEnum.COMMENT_NOT_FOUND)
        );
        if (comment.getUsers().getUsername().equals(users.getUsername()) || users.getRole() == UserRoleEnum.ADMIN) {
            commentRepository.delete(comment);

            return new StatusResponseDto("해당 댓글을 삭제하였습니다.", HttpStatus.OK.value());
        } else {
            return new ErrorResponseDto(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
        }
    }

    private Users getUserByToken(String token) {
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ErrorException(ExceptionEnum.TOKEN_NOT_FOUND);
            }

            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
            );
        }
        return null;
    }
}