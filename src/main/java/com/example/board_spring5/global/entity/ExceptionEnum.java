package com.example.board_spring5.global.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

    USERS_DUPLICATION(HttpStatus.BAD_REQUEST, "중복된 이름이 존재합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 회원이 존재 하지 않습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "관리자 암호를 잘못 입력하셨습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 잘못 입력하셨습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 유효하지 않습니다."),
    NOT_ALLOW_AUTHORIZATIONS(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다.");


    private final HttpStatus status;
    private final String message;
}
