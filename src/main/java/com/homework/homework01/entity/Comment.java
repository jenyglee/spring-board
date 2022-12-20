package com.homework.homework01.entity;

import com.homework.homework01.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String content;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    public Comment(Board board, CommentRequestDto requestDto, User user) {
        this.password = requestDto.getPassword();
        this.content = requestDto.getContent();
        this.board = board;
        this.user = user;
    }

    public void validPassword(String password){
        if(!this.password.equals(password)){
            throw new IllegalArgumentException("댓글의 비밀번호가 틀렸습니다.");
        };
    }

    public void update(CommentRequestDto requestDto) {
        System.out.println("requestDto.getContent() : " + requestDto.getContent());
        this.content = requestDto.getContent();
    }
}
