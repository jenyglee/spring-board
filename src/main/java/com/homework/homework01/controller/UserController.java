package com.homework.homework01.controller;

import com.homework.homework01.dto.LoginRequestDto;
import com.homework.homework01.dto.SignupRequestDto;
import com.homework.homework01.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto signupRequestDto){
        userService.signup(signupRequestDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){ //✨http request를 받을 때 헤더가 넘어오듯이 우리가 response 객체를 반환할 때는 HttpServletResponse를 이용해서 반환한다.
        userService.login(loginRequestDto, response);
    }
}
