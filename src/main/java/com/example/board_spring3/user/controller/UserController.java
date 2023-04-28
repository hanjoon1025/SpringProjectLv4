package com.example.board_spring3.user.controller;

import com.example.board_spring3.global.dto.ResponseDto;
import com.example.board_spring3.global.dto.StatusResponseDto;
import com.example.board_spring3.user.dto.UserRequestDto;
import com.example.board_spring3.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseBody
    @PostMapping("/signup")
    public StatusResponseDto signup(@RequestBody UserRequestDto userRequestDto){
        return userService.signUp(userRequestDto);
    }
    @ResponseBody
    @PostMapping("/login")
    public StatusResponseDto login (@RequestBody UserRequestDto userRequestDto, HttpServletResponse httpServletResponse){
        return userService.login(userRequestDto, httpServletResponse);
    }
}
