package com.homework.homework01.dto;

import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> commentList = new ArrayList<>();



    public BoardResponseDto(Board board, List<Comment> comments) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getUsername();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        for (Comment comment:comments){
            commentList.add(new CommentResponseDto(comment));
        }
        // if(board.getCommentList() != null){
        //     List<CommentResponseDto> commentList = new ArrayList<>();
        //     for (Comment comment : board.getCommentList()){
        //         commentList.add(new CommentResponseDto(comment));
        //     }
        //     this.commentList = commentList;
        // }else{
        //     this.commentList = null;
        // }

    }

    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getUsername();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
