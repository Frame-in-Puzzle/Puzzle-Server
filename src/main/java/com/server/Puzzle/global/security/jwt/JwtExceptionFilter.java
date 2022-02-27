package com.server.Puzzle.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.server.Puzzle.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        try {
            chain.doFilter(req, res);
        } catch (ExpiredJwtException ex) {
            log.error("============= 토큰의 유효기간이 만료 =============", ex);
            setErrorResponse(EXPIRED_TOKEN, res);
        } catch (JwtException ex) {
            log.error("============= 유효하지 않은 토큰 =============", ex);
            setErrorResponse(INVALID_TOKEN, res);
        } catch (Exception e) {
            log.error("============= 알 수 없는 에러 발생 =============", e);
            setErrorResponse(UNKNOWN_ERROR, res);
        }
    }

    private void setErrorResponse(ErrorCode errorCode, HttpServletResponse res) throws IOException {
        res.setStatus(errorCode.getStatus());
        res.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getDetail())
                .build();
        String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
        res.getWriter().write(errorResponseEntityToJson.toString());
    }
}