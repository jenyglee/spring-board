package com.homework.homework01.controller;

import com.homework.homework01.dto.CommentRequestDto;
import com.homework.homework01.dto.CommentResponseDto;
import com.homework.homework01.entity.User;
import com.homework.homework01.jwtUtil.JwtUtil;
import com.homework.homework01.repository.UserRepository;
import com.homework.homework01.service.CommentService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class CommentController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CommentService commentService;
    // 댓글 작성
    @PostMapping("/{id}/comments")
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


    // 댓글 수정
    @PutMapping("/{boardId}/comments/{commentId}")
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
    @DeleteMapping("/{boardId}/comments/{commentId}")
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
