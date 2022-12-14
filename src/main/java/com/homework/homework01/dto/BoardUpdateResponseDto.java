package com.homework.homework01.dto;

import com.homework.homework01.entity.Board;
import lombok.Getter;

@Getter
public class BoardUpdateResponseDto {
    private BoardResponseDto responseDto;
    private boolean success;
    private String message;

    public BoardUpdateResponseDto(Board board, boolean success, String message) {
        this.responseDto = new BoardResponseDto(board);
        this.success = success;
        this.message = message;
    }

    public BoardUpdateResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
