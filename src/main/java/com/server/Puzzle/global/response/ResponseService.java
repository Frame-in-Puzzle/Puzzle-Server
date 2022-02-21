package com.server.Puzzle.global.response;

import com.server.Puzzle.global.response.result.CommonResult;
import com.server.Puzzle.global.response.result.ListResult;
import com.server.Puzzle.global.response.result.SingleResult;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Getter
    public enum CommonResponse{
        SUCCESS(1, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String message;

        CommonResponse(int code, String message){
            this.code = code;
            this.message = message;
        }
    }

    public CommonResult getSuccessResult() {
        return CommonResult.builder()
                .success(true)
                .code(CommonResponse.SUCCESS.getCode())
                .message(CommonResponse.SUCCESS.getMessage())
                .build();
    }

    public <T> SingleResult<T> getSingleResult(T data){
        return new SingleResult<T>(getSuccessResult(), data);
    }

    public <T> ListResult<T> getListResult(List<T> list){
        return new ListResult<T>(getSuccessResult(), list);
    }

    public CommonResult getFailResult(int code, String msg) {
        return CommonResult.builder()
                .success(false)
                .code(code)
                .message(msg)
                .build();
    }
}
