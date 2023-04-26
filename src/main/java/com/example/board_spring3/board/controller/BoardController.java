package com.example.board_spring3.board.controller;

import com.example.board_spring3.global.dto.ResponseDto;
import com.example.board_spring3.board.dto.BoardRequestDto;
import com.example.board_spring3.board.dto.BoardResponseDto;
import com.example.board_spring3.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public BoardResponseDto createBoard (@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest){
        return boardService.createBoard(boardRequestDto, httpServletRequest);
    }

    @GetMapping
    public List<BoardResponseDto> getBoardList(){
        return boardService.getBoardList();
    }
    @GetMapping("/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PutMapping("/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest){
        return boardService.updateBoard(id, boardRequestDto, httpServletRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteBoard (@PathVariable Long id, HttpServletRequest httpServletRequest){
        return boardService.deleteBoard(id, httpServletRequest);
    }
}


