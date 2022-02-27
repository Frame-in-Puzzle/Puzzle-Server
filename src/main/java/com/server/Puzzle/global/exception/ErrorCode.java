package com.server.Puzzle.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(500, "Unknown Error"),
    USER_NOT_FOUND(404, "Not Found User"),
    BOARD_NOT_FOUND(404, "Not Found Board"),
    BOARD_NOT_HAVE_PERMISSION(403, "Forbidden"),
    UNAUTHORIZED_USER(401, "Unauthorized User"),
    EXPIRED_TOKEN(401, "Token is Expired"),
    INVALID_TOKEN(401, "Invalid Token"),
    PARAMETER_IS_MISSING(400, "Parameter is Missing"),
    IS_ALREADY_ATTEND(400,"Is Already Attend"),
    ATTEND_PATCH_PERMISSION_DENIED(401, "Attend Patch Permission Denied");

    private final int status;
    private final String detail;
}
