package com.example.board_spring3.comment.service;

import com.example.board_spring3.comment.dto.CommentRequestDto;
import com.example.board_spring3.comment.dto.CommentResponseDto;
import com.example.board_spring3.board.entity.Board;
import com.example.board_spring3.comment.entity.Comment;
import com.example.board_spring3.user.entity.Users;
import com.example.board_spring3.global.jwt.JwtUtil;
import com.example.board_spring3.board.repository.BoardRepository;
import com.example.board_spring3.comment.repository.CommentRepository;
import com.example.board_spring3.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            Users users = checkUsers(claims);
            Board board = checkBord(commentRequestDto.getBoard_id());

            Comment comment = new Comment(users,board, commentRequestDto);
            commentRepository.save(comment);

            return new CommentResponseDto(comment);
        }
        throw new IllegalArgumentException("게시글을 작성 할 수 없습니다.");
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            Users users = checkUsers(claims);
            Comment comment = checkComment(id);

            comment.setContent(commentRequestDto.getContent());

            return new CommentResponseDto(comment);
        }
        throw new IllegalArgumentException("댓글을 작성 할 수 없습니다.");
    }
    @Transactional
    public CommentResponseDto deleteComment(Long id, HttpServletRequest httpServletRequest){
        return new CommentResponseDto(null);
    }

    private Users checkUsers(Claims claims){
        Users users = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        return users;
    }

    private Board checkBord(Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return board;
    }

    private Comment checkComment(Long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }
}