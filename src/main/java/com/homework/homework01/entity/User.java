package com.homework.homework01.entity;

import com.homework.homework01.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private UserRoleEnum role;

    public User(SignupRequestDto signupRequestDto, UserRoleEnum role) {
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.role = role;
    }
}
