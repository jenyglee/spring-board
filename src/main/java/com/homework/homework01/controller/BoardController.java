package com.homework.homework01.controller;

import com.homework.homework01.dto.*;
import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.Comment;
import com.homework.homework01.entity.User;
import com.homework.homework01.jwtUtil.JwtUtil;
import com.homework.homework01.repository.UserRepository;
import com.homework.homework01.service.BoardService;
import com.homework.homework01.service.CommentService;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return boardService.createBoard(requestDto, user);
        } else {
            return null;
        }

    }

    // 게시글 수정
    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) throws IllegalArgumentException {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return boardService.updateBoard(id, requestDto, user);
        }
        return null;
    }

    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if(token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("Token Error");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            boardService.deleteBoard(id, user);
        }

    }

    // 댓글 작성
    @PostMapping("/board/{id}/comment")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return commentService.createComment(id, requestDto, user);

        } else {
            return null;
        }

    }

    // 댓글 조회
    @GetMapping("/board/{id}/comment")
    public List<CommentResponseDto> getComments(@PathVariable Long id) {
        return commentService.getComments(id);
    }

    // 댓글 수정
    @PutMapping("/board/{boardId}/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return commentService.updateComment(boardId, commentId, requestDto, user);
        } else {
            return null;
        }

    }

    // 댓글 삭제
    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long boardId, @PathVariable Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            commentService.deleteComment(boardId, commentId, user);
        }

    }

}
