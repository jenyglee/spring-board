package com.homework.homework01.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private Long id;
    private String title;
    private String name;
    private String content;
    private String password;
}
