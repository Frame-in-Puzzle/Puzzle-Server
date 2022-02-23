package com.server.Puzzle.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


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
    PARAMETER_IS_MISSING(400, "Parameter is Missing");

    private final int status;
    private final String detail;
}
