package com.example.board_spring3.user.service;

import com.example.board_spring3.global.dto.InterfaceDto;
import com.example.board_spring3.global.dto.StatusResponseDto;
import com.example.board_spring3.global.exception.ErrorException;
import com.example.board_spring3.global.exception.ErrorResponseDto;
import com.example.board_spring3.global.exception.ExceptionEnum;
import com.example.board_spring3.global.jwt.JwtUtil;
import com.example.board_spring3.user.dto.UserRequestDto;
import com.example.board_spring3.user.entity.UserRoleEnum;
import com.example.board_spring3.user.entity.Users;
import com.example.board_spring3.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final JwtUtil jwtUtil;

    @Transactional
    public InterfaceDto signUp(UserRequestDto userRequestDto) {
        Optional<Users> check = userRepository.findByUsername(userRequestDto.getUsername());
        if (check.isPresent()) {
            return new ErrorResponseDto(ExceptionEnum.USERS_DUPLICATION);
        }
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new ErrorException(ExceptionEnum.TOKEN_NOT_FOUND);
            }
            userRoleEnum = UserRoleEnum.ADMIN;
        }
        Users users = new Users(userRequestDto, userRoleEnum);
        userRepository.save(users);
        return new StatusResponseDto("회원가입 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public InterfaceDto login(UserRequestDto userRequestDto, HttpServletResponse httpServletResponse) {
        try {
            String username = userRequestDto.getUsername();
            String password = userRequestDto.getPassword();

            Users users = userRepository.findByUsername(username).orElseThrow(
                    () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
            );

            if (!users.getPassword().equals(password)) {
                throw new ErrorException(ExceptionEnum.INVALID_PASSWORD);
            }
            httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(users.getUsername(), users.getRole()));

            return new StatusResponseDto("로그인 성공", HttpStatus.OK.value());
        } catch (ErrorException e) {
            return new ErrorResponseDto(e.getExceptionEnum().getMessage(), e.getExceptionEnum().getStatus());
        }
    }
}

