package com.example.board_spring3.user.service;

import com.example.board_spring3.global.dto.ResponseDto;
import com.example.board_spring3.global.dto.StatusResponseDto;
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
    public StatusResponseDto signUp(UserRequestDto userRequestDto) {
        Optional<Users> check = userRepository.findByUsername(userRequestDto.getUsername());
        if (check.isPresent()) {
            return new StatusResponseDto("같은 아이디가 이미 있습니다.", HttpStatus.ALREADY_REPORTED);
        }
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            userRoleEnum = UserRoleEnum.ADMIN;
        }
        Users users = new Users(userRequestDto, userRoleEnum);
        userRepository.save(users);
        return new StatusResponseDto("회원가입 성공", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public StatusResponseDto login(UserRequestDto userRequestDto, HttpServletResponse httpServletResponse) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        Users users = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!users.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(users.getUsername(), users.getRole()));

        return new StatusResponseDto("로그인 성공", HttpStatus.OK);
    }
}
