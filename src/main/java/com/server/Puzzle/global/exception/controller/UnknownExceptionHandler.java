package com.server.Puzzle.global.exception.controller;

import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UnknownExceptionHandler {
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(Exception e) {
        log.error("UnknownExceptionHandler throw Exception : {}", e.getMessage(), e);
        return ErrorResponse.toResponseEntity(ErrorCode.UNKNOWN_ERROR);
    }
}
