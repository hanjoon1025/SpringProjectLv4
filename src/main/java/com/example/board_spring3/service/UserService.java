package com.example.board_spring3.service;

import com.example.board_spring3.dto.ResponseDto;
import com.example.board_spring3.dto.user.LoginRequestDto;
import com.example.board_spring3.dto.user.SignupRequestDto;
import com.example.board_spring3.entity.UserRoleEnum;
import com.example.board_spring3.entity.Users;
import com.example.board_spring3.jwt.JwtUtil;
import com.example.board_spring3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto signUp(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        if(username.length() < 4 || username.length() > 10 || !Pattern.matches("[a-z0-9]*$", username)){
            return new ResponseDto("아이디를 다시 입력해 주세요.", 100);
        }
        if(password.length() < 8 || password.length() > 15 || !Pattern.matches("[A-Za-z0-9]*$", password)){
            return new ResponseDto("비밀번호를 다시 입력해 주세요.",100);
        }

        //회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()){
            return new ResponseDto("같은 아이디가 이미 있습니다.", 100);
        }

        //사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()){
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        Users users = new Users(username, password, role);
        userRepository.save(users);

        ResponseDto responseDto = new ResponseDto("회원가입 성공", 200);
        return new ResponseDto("회원가입 성공", 200);
    }

    @Transactional(readOnly = true)
    public ResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        Users users = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        if(!users.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(users.getUsername()));

        return new ResponseDto("로그인 성공", 200);
    }
}
