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
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 현재 로그인 된 유저를 로그아웃함
     * @Header Bearer AccessToken
     * @return ResponseEntity - Success
     * @author 홍현인
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @DeleteMapping("/logout")
    public ResponseEntity logout() {
        userService.logout();
        return ResponseEntity.ok("Success");
    }

    /**
     * 현재 로그인 한 회원탈퇴(계정삭제)
     * @Header Bearer AccessToken
     * @return ResponseEntity - Success
     * @author 홍현인
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @DeleteMapping("/delete")
    public ResponseEntity delete() {
        userService.delete();
        return ResponseEntity.ok("Success");
    }

    /**
     * 첫 회원가입을 한 유저의 정보(분야, 언어) 등록
     * @Header Bearer AccessToken
     * @param userInfo name, email, imageUrl, bio, field, languages
     * @return ResponseEntity - Success
     * @author 홍현인
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/registration")
    public ResponseEntity infoRegister(@RequestBody UserUpdateDto userInfo) {
        userService.infoRegistration(userInfo);
        return ResponseEntity.ok("Success");
    }

}
