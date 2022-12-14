package com.homework.homework01.service;

import com.homework.homework01.dto.LoginRequestDto;
import com.homework.homework01.dto.SignupRequestDto;
import com.homework.homework01.entity.User;
import com.homework.homework01.jwtUtil.JwtUtil;
import com.homework.homework01.repository.UserRepository;
import com.homework.homework01.jwtUtil.JwtUtil;
import com.homework.homework01.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //✨최소, 최대, 소문자+숫자 구성 제한하기
    //✨성공했다는 메시지, 상태코드 반환하기
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        //username 중복확인
        String username = signupRequestDto.getUsername();
        System.out.println("username : " + username);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        userRepository.save(new User(signupRequestDto));
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // DB에 저장된 아이디가 있는지 확인
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new IllegalArgumentException("등록된 사용자가 없습니다.");
        }
        // 비밀번호가 같은지 확인
        if(!user.get().getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 로그인 성공시 토큰 발급
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(username));
    }
}
