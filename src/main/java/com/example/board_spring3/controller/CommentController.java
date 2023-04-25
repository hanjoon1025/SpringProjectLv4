package com.example.board_spring3.controller;

import com.example.board_spring3.dto.comment.CommentRequestDto;
import com.example.board_spring3.dto.comment.CommentResponseDto;
import com.example.board_spring3.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponseDto createComment (CommentRequestDto commentRequestDto,HttpServletRequest request){
        return commentService.createComment(commentRequestDto,request);
    }

    @PutMapping("/{id}")
    public CommentResponseDto updateComment (@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest){
        return commentService.updateComment(id, commentRequestDto, httpServletRequest);
    }

    @DeleteMapping("/{id}")
    public CommentResponseDto deleteComment (@PathVariable Long id, HttpServletRequest httpServletRequest){
        return commentService.deleteComment(id, httpServletRequest);
    }
}
