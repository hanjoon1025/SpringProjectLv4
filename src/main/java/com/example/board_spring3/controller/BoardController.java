package com.example.board_spring3.controller;

import com.example.board_spring3.dto.ResponseDto;
import com.example.board_spring3.dto.board.BoardRequestDto;
import com.example.board_spring3.dto.board.BoardResponseDto;
import com.example.board_spring3.service.BoardService;
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


