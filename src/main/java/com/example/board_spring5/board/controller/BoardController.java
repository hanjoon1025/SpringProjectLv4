package com.example.board_spring5.board.controller;

import com.example.board_spring5.board.dto.BoardRequestDto;
import com.example.board_spring5.board.dto.BoardResponseDto;
import com.example.board_spring5.board.service.BoardService;
import com.example.board_spring5.global.dto.InterfaceDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // indicates that this class is a REST controller that is used to handle HTTP requests and responses.
@RequestMapping("/api/board") //specifies that this controller should handle requests with the URL path prefix "/api/board"
@RequiredArgsConstructor // generates a constructor for the class that initializes all final fields
public class BoardController {

    private final BoardService boardService; //@RequiredArgsConstructor generates a constructor that initializes this field

    @PostMapping // handles HTTP POST requests
    public InterfaceDto createBoard (@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest){
        return boardService.createBoard(boardRequestDto, httpServletRequest);
    }

    @GetMapping
    public List<BoardResponseDto> getBoardList(){
        return boardService.getBoardList();
    }

    @GetMapping("/{id}")
    public InterfaceDto getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PutMapping("/{id}")
    public InterfaceDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest httpServletRequest){
        return boardService.updateBoard(id, boardRequestDto, httpServletRequest);
    }

    @DeleteMapping("/{id}")
    public InterfaceDto deleteBoard (@PathVariable Long id, HttpServletRequest httpServletRequest){
        return boardService.deleteBoard(id, httpServletRequest);
    }
}

// serialization(직렬화) and deserialization(역직렬화)
// serialization is a mechanism of converting the state of an object into a byte stream.
// deserialization is the reverse process where the byte stream is used to recreate the actual Java object in memory
// 직렬화는 객체들의 데이터를 연속적인 데이터로 변형하여 Stream을 통해 데이터를 읽도록 해줌
// 역직렬화는 직렬화된 파일 등을 역으로 직렬화하여 다시 객체의 형태로 만듬 -> 저장된 파일을 읽거나 전송된 Stream 데이터를 읽어 원래 객체의 형태로 복원


