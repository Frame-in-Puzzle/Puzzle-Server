package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/api/user")
@RestController
public class UserController {
    private final UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @DeleteMapping("/logout")
    public ResponseEntity logout() {
        userService.logout();
        return ResponseEntity.ok("Success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @DeleteMapping("/delete")
    public ResponseEntity delete() {
        userService.delete();
        return ResponseEntity.ok("Success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/register")
    public ResponseEntity infoRegister(@RequestBody UserUpdateDto userInfo) {
        userService.infoRegister(userInfo);
        return ResponseEntity.ok("Success");
    }
}
