package com.example.board_spring3.board.service;

import com.example.board_spring3.board.dto.BoardRequestDto;
import com.example.board_spring3.board.dto.BoardResponseDto;
import com.example.board_spring3.board.entity.Board;
import com.example.board_spring3.board.repository.BoardRepository;
import com.example.board_spring3.comment.dto.CommentResponseDto;
import com.example.board_spring3.comment.entity.Comment;
import com.example.board_spring3.global.dto.InterfaceDto;
import com.example.board_spring3.global.dto.StatusResponseDto;
import com.example.board_spring3.global.jwt.JwtUtil;
import com.example.board_spring3.user.entity.UserRoleEnum;
import com.example.board_spring3.user.entity.Users;
import com.example.board_spring3.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public InterfaceDto createBoard(BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);

        Users users = getUserByToken(token);

        if (users != null){
        Board board = new Board(boardRequestDto, users);

        boardRepository.save(board);
        return new BoardResponseDto(board);
        } else {
            return new StatusResponseDto("Token Error", HttpStatus.BAD_REQUEST.value());
        }
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comment : board.getComment()) {
            comments.add(new CommentResponseDto(comment));
        }

        return new BoardResponseDto(board, comments);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardList() {
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();

        List<BoardResponseDto> boards = new ArrayList<>();

        for (Board board : boardList){
            List<CommentResponseDto> comments = new ArrayList<>();
            for (Comment comment : board.getComment()) {
                comments.add(new CommentResponseDto(comment));
            }
            boards.add(new BoardResponseDto(board, comments));
        }
        return boards;
    }

    @Transactional
    public InterfaceDto updateBoard(Long id, BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);

        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다")
        );

        Users users = getUserByToken(token);

        if(board.getUsers().getUsername().equals(users.getUsername()) || users.getRole() == UserRoleEnum.ADMIN){
            board.update(boardRequestDto);
        } else {
            return new StatusResponseDto("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST.value());
        }
        return new BoardResponseDto(board);
    }

    public StatusResponseDto deleteBoard(Long id, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);

        Claims claims = checkToken(httpServletRequest);

        Users users = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다")
        );

        if(users.getUsername().equals(board.getUsers().getUsername()) || users.getRole() == UserRoleEnum.ADMIN){
            boardRepository.deleteById(board.getId());
        } else {
            return new StatusResponseDto("해당 게시글을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST.value());
        }
        return new StatusResponseDto("게시글을 삭제하였습니다.", HttpStatus.OK.value());
    }

    private Users getUserByToken(String token) {
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("존재하지 않는 사용자입니다.")
            );
        }
        return null;
    }

    private Claims checkToken(HttpServletRequest httpServletRequest) throws IllegalArgumentException {
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(httpServletRequest));
        if(claims == null){
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return claims;
    }
}