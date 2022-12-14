package com.homework.homework01.dto;

import com.homework.homework01.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;



    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getUsername();
        this.createdAt = board.getCreatedAt();
    }
}
