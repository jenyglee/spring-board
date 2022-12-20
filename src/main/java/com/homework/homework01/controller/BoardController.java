package com.homework.homework01.controller;

import com.homework.homework01.dto.*;
import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.Comment;
import com.homework.homework01.service.BoardService;
import com.homework.homework01.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    // 게시글 전체 조회
    @GetMapping("/board")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    // 게시글 단건 조회
    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 게시글 작성
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.createBoard(requestDto, request);
    }

    // 게시글 수정
    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.updateBoard(id, requestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id, @RequestBody BoardDeleteRequestDto requestDto, HttpServletRequest request) {
        boardService.deleteBoard(id, requestDto, request);
    }

    // 댓글 작성
    @PostMapping("/board/{id}/comment")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    // 댓글 조회
    @GetMapping("/board/{id}/comment")
    public List<CommentResponseDto> getComments(@PathVariable Long id) {
        return commentService.getComments(id);
    }

    // 댓글 수정
    @PutMapping("/board/{boardId}/comment/{commentId}")
    public void updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        commentService.updateComment(boardId, commentId, requestDto, request);
    }

    //댓글 삭제
    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long boardId, @PathVariable Long commentId, HttpServletRequest request){
        commentService.deleteComment(boardId, commentId, request);
    }

}
