package com.example.board_spring3.service;

import com.example.board_spring3.dto.comment.CommentRequestDto;
import com.example.board_spring3.dto.comment.CommentResponseDto;
import com.example.board_spring3.entity.Board;
import com.example.board_spring3.entity.Comment;
import com.example.board_spring3.entity.Users;
import com.example.board_spring3.jwt.JwtUtil;
import com.example.board_spring3.repository.BoardRepository;
import com.example.board_spring3.repository.CommentRepository;
import com.example.board_spring3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            Users users = checkUsers(claims);
            Board board = checkBord(commentRequestDto.getBoard_id());
        }
        return null;
    }
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            Users users = checkUsers(claims);
            Comment comment = checkComment(id);

            if (users.getUsername().equals(comment.getUsers().getUsername())) {
                comment.update(commentRequestDto);
            }
            return new CommentResponseDto(comment);
        }else {
            return null;
        }
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
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        return comment;
    }


}



