package com.example.board_spring3.service;

import com.example.board_spring3.dto.BoardRequestDto;
import com.example.board_spring3.dto.BoardResponseDto;
import com.example.board_spring3.dto.ResponseDto;
import com.example.board_spring3.entity.Board;
import com.example.board_spring3.entity.User;
import com.example.board_spring3.jwt.JwtUtil;
import com.example.board_spring3.repository.BoardRepository;
import com.example.board_spring3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = checkUser(claims);

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Board board = new Board(boardRequestDto, user.getId(), user.getUsername());
            Long id = boardRepository.saveAndFlush(board).getId();

            return new BoardResponseDto(checkBoard(id));
        } else {
            return null;
        }
    }

    public BoardResponseDto getBoard(Long id) {
        return new BoardResponseDto(checkBoard(id));
    }

    public List<BoardResponseDto> getBoardList() {
        //findAll()은 List<Board> 형으로 반환 ->stream을 통해 매핑 -> new BoardResponseDto로 리턴타입 맞추기
        return boardRepository.findAll().stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = checkUser(claims);
            Board board = checkBoard(id);

            if (board.getUsername().equals(user.getUsername())) {
                board.update(boardRequestDto);
            }

            return new BoardResponseDto(checkBoard(id));
        } else {
            return null;
        }
    }

    public ResponseDto deleteBoard(Long id, HttpServletRequest httpServletRequest) {

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = checkUser(claims);
           Board board = checkBoard(id);

            if (board.getUsername().equals(user.getUsername())) {
                boardRepository.delete(board);
            }

            return new ResponseDto("삭제 성공", 200);
        } else {
            return new ResponseDto("삭제 실패",100);
        }
    }

    private Board checkBoard(Long id){
        return  boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 포스트가 없습니다!!!")
        );
    }
    private User checkUser(Claims claims) {
        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다!!!")
        );
    }
}