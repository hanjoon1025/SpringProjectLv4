package com.example.board_spring3.user.controller;

import com.example.board_spring3.global.dto.ResponseDto;
import com.example.board_spring3.user.dto.LoginRequestDto;
import com.example.board_spring3.user.dto.SignupRequestDto;
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
    public ResponseDto signup(@RequestBody SignupRequestDto signupRequestDto){
        return userService.signUp(signupRequestDto);
    }
    @ResponseBody
    @PostMapping("/login")
    public ResponseDto login (@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return userService.login(loginRequestDto, response);
    }
}
