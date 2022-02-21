package com.server.Puzzle.global.response.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {
    private boolean success;
    private String message;
    private int code;

    public CommonResult(CommonResult commonResult) {
        this.success = commonResult.success;
        this.message = commonResult.message;
        this.code = commonResult.code;
    }
}
