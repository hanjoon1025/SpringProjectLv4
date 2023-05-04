package com.example.board_spring5.board.service;

import com.example.board_spring5.board.dto.BoardRequestDto;
import com.example.board_spring5.board.dto.BoardResponseDto;
import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.board.repository.BoardRepository;
import com.example.board_spring5.comment.dto.CommentResponseDto;
import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.global.dto.InterfaceDto;
import com.example.board_spring5.global.dto.StatusResponseDto;
import com.example.board_spring5.global.exception.ErrorException;
import com.example.board_spring5.global.exception.ErrorResponseDto;
import com.example.board_spring5.global.exception.ExceptionEnum;
import com.example.board_spring5.global.jwt.JwtUtil;
import com.example.board_spring5.user.entity.UserRoleEnum;
import com.example.board_spring5.user.entity.Users;
import com.example.board_spring5.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service //
@RequiredArgsConstructor // generates a constructor for the class that initializes all final fields
public class BoardService {

    //주입
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public InterfaceDto createBoard(BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest) {
        try {
            String token = jwtUtil.resolveToken(httpServletRequest);
            Users users = getUserByToken(token);
            if (users != null) {
                Board board = new Board(boardRequestDto, users);
                boardRepository.save(board);
                return new BoardResponseDto(board);
            } else {
                return new ErrorResponseDto(ExceptionEnum.TOKEN_NOT_FOUND);
            }
        } catch (ErrorException e) {
            return new ErrorResponseDto(e.getExceptionEnum().getMessage(), e.getExceptionEnum().getStatus());
        }
    }

    public InterfaceDto getBoard(Long id) { // 특정 게시글을 id로 가져오기 - id와 매칭하는 BoardResponseDto에 있는 게시글을 가져옴
        try {
            Board board = boardRepository.findById(id).orElseThrow( // boardRepository findById를 사용하여 DB Board에서 검색
                    () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND) // 못 찾을 경우 메세지 output
            );

            // 보드 확인 후 댓글 연결
            List<CommentResponseDto> comments = new ArrayList<>();
            for (Comment comment : board.getComment()) {
                comments.add(new CommentResponseDto(comment));
            }

            return new BoardResponseDto(board, comments);
        } catch (ErrorException e) {
            return new ErrorResponseDto(e.getExceptionEnum().getMessage(),e.getExceptionEnum().getStatus());
        }
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

        Board board = null;
        try {
            board = boardRepository.findById(id).orElseThrow(
                    ()-> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
            );
        } catch (ErrorException e) {
            return new ErrorResponseDto(e.getExceptionEnum());
        }

        Users users = null;
        try {
            users = getUserByToken(token);
        } catch (ErrorException e) {
            return new ErrorResponseDto(e.getExceptionEnum());
        }

        try {
            if(board.getUsers().getUsername().equals(users.getUsername()) || users.getRole() == UserRoleEnum.ADMIN){
                board.update(boardRequestDto);
                return new BoardResponseDto(board);
            } else {
                return new ErrorResponseDto(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
            }
        } catch (Exception e) {
            return new ErrorResponseDto(ExceptionEnum.UNKNOWN_ERROR);
        }
    }

    public InterfaceDto deleteBoard(Long id, HttpServletRequest httpServletRequest) {

        try {
            String token = jwtUtil.resolveToken(httpServletRequest);

            Claims claims = checkToken(httpServletRequest);

            Users users = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
            );

            Board board = boardRepository.findById(id).orElseThrow(
                    ()-> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
            );

            if(users.getUsername().equals(board.getUsers().getUsername()) || users.getRole() == UserRoleEnum.ADMIN){
                boardRepository.deleteById(board.getId());
            } else {
                return new ErrorResponseDto(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
            }
            return new StatusResponseDto("게시글을 삭제하였습니다.", HttpStatus.OK.value());
        } catch (ErrorException e) {
            return new ErrorResponseDto(e.getExceptionEnum());
        } catch (Exception e) {
            return new ErrorResponseDto(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    private Users getUserByToken(String token) {
        try {
            Claims claims;

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new ErrorException(ExceptionEnum.TOKEN_NOT_FOUND);
                }

                return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        ()-> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
                );
            }
            return null;
        } catch (ErrorException e) {
            throw new ErrorException(e.getExceptionEnum());
        }
    }


    private Claims checkToken(HttpServletRequest httpServletRequest) {
        try {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(httpServletRequest));
            if (claims == null) {
                throw new ErrorException(ExceptionEnum.TOKEN_NOT_FOUND);
            }
            return claims;
        } catch (ErrorException e) {
            throw new ErrorException(e.getExceptionEnum());
        }
    }
}