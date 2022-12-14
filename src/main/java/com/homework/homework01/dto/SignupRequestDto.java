package com.homework.homework01.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
@Getter
public class SignupRequestDto {

    @Pattern(regexp= "^(?=.*[a-z])((?=.*\\d)).{4,10}$")
    @Size(min = 4, max = 10 , message = "이름은 4~10자리로 입력해주세요.")
    private String username;
    @Pattern(regexp= "^(?=.*[a-z])((?=.*\\d)).{4,15}$")
    @Size(min = 4, max = 15, message = "비밀번호는 4~15자리로 입력해주세요.")
    private String password;
}
