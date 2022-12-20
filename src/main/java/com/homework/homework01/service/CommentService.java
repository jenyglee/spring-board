package com.homework.homework01.service;

import com.homework.homework01.dto.CommentRequestDto;
import com.homework.homework01.dto.CommentResponseDto;
import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.Comment;
import com.homework.homework01.entity.User;
import com.homework.homework01.jwtUtil.JwtUtil;
import com.homework.homework01.repository.BoardRepository;
import com.homework.homework01.repository.CommentRepository;
import com.homework.homework01.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        List<Comment> commentList =  board.getCommentList();
        List<CommentResponseDto> list = new ArrayList<>();
        for (Comment comment : commentList){
            list.add(new CommentResponseDto(comment));
        }

        return list;
    }

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        // ✅토큰에서 유저정보를 빼내어 함께 저장해야함.
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
            // id를 이용해서 게시글을 단건 조회한다.
            Board board = boardRepository.findById(id).orElseThrow(
                    ()-> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );

            // 조회된 게시글의 commentList에 추가한다.
            Comment comment = new Comment(board, requestDto, user);
            List<Comment> commentList = board.getCommentList();
            commentList.add(comment);

            // commentList를 BoardRepository에 저장한다.
            commentRepository.save(comment);
            return new CommentResponseDto(comment);
        }else{
            return null;
        }

    }

    @Transactional
    public void updateComment(Long boardId, Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
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
            Board board = boardRepository.findById(boardId).orElseThrow(
                    ()-> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );
            Comment comment = commentRepository.findByIdAndBoard_IdAndUser_Id(commentId, board.getId(), user.getId()).orElseThrow(
                    ()-> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );
            comment.validPassword(requestDto.getPassword());
            comment.update(requestDto);
        }
    }

    public void deleteComment(Long boardId, Long commentId, HttpServletRequest request) {
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
            Board board = boardRepository.findById(boardId).orElseThrow(
                    ()-> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );
            commentRepository.deleteById(commentId);
            // Optional<Comment> comment = commentRepository.findByIdAndBoard_IdAndUser_Id(commentId, board.getId(), user.getId());
            // if (comment.isPresent()){
            //     commentRepository.deleteById(commentId);
            // }else{
            //     throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
            // }

        }
    }
}
