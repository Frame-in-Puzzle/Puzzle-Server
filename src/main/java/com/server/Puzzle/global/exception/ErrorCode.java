package com.server.Puzzle.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저를 찾을 수 없습니다"),
    BOARD_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_USER(UNAUTHORIZED, "인증되지 않은 사용자입니다"),
    BOARD_NOT_HAVE_PERMISSION_TO_DELETE(UNAUTHORIZED, "해당 게시글을 삭제할 권한이 없습니다"),
    BOARD_NOT_HAVE_PERMISSION_TO_MODIFY(UNAUTHORIZED, "해당 게시글을 수정할 권한이 없습니다"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    PARAMETER_IS_MISSING(BAD_REQUEST, "잘못된 경로로 접근하였습니다");

    private final int status;
    private final String detail;
}
