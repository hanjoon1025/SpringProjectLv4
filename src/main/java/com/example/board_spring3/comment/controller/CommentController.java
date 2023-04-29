package com.example.board_spring3.comment.controller;

import com.example.board_spring3.comment.dto.CommentRequestDto;
import com.example.board_spring3.comment.service.CommentService;
import com.example.board_spring3.global.dto.InterfaceDto;
import com.example.board_spring3.global.dto.StatusResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public InterfaceDto createComment (@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        return commentService.createComment(commentRequestDto, httpServletRequest);
    }

    @PutMapping("/{id}")
    public InterfaceDto updateComment (@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest){
        return commentService.updateComment(id, commentRequestDto, httpServletRequest);
    }

    @DeleteMapping("/{id}")
    public InterfaceDto deleteComment (@PathVariable Long id, HttpServletRequest httpServletRequest){
        return commentService.deleteComment(id, httpServletRequest);
    }
}
