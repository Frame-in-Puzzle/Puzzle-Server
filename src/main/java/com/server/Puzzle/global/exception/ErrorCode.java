package com.server.Puzzle.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(500, "알 수 없는 에러입니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    BOARD_NOT_HAVE_PERMISSION(403, "해당 요청을 처리할 권한이 존재하지 않습니다."),
    UNAUTHORIZED_USER(401, "인증되지 않은 사용자입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    UNABLE_TO_ISSUANCE_REFRESHTOKEN(400, "유효하지 않은 리프레쉬 토큰입니다."),
    NOT_LOGGED_IN(400, "로그인 되어있지 않습니다."),
    PARAMETER_IS_MISSING(400, "잘못된 경로입니다."),
    IS_ALREADY_USER(400, "이미 존재하는 사용자입니다."),
    IS_ALREADY_ATTEND(400,"이미 신청되어 있습니다"),
    ATTEND_PATCH_PERMISSION_DENIED(401, "신청을 수정할 권한이 존재하지 않습니다."),
    ATTEND_NOT_FOUND(404, "신청을 찾을 수 없습니다."),
    ATTEND_DELETE_PERMISSION_DENIED(401, "신청을 삭제할 권한이 존재하지 않습니다.");

    private final int status;
    private final String detail;

}
