package com.homework.homework01.dto;

import lombok.Getter;

@Getter
public class BoardDeleteResponseDto {
    private boolean deleted;
    private String massage;

    public BoardDeleteResponseDto(boolean deleted, String message) {
        this.deleted = deleted;
        this.massage = message;
    }
}
