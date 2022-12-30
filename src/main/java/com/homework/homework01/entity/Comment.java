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
    private String content;
    // @ManyToOne
    // @JoinColumn(name = "USER_ID", nullable = false)
    // private User user;

    private String username;

    // @ManyToOne
    // @JoinColumn(name = "BOARD_ID", nullable = false)
    // private Board board;

    private Long boardId;

    public Comment(Long boardId, String username, CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
        // this.board = board;
        // this.user = user;
        this.boardId = boardId;
        this.username = username;
    }


    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
