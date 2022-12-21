package com.homework.homework01.service;

import com.homework.homework01.dto.BoardResponseDto;
import com.homework.homework01.dto.CommentRequestDto;
import com.homework.homework01.dto.CommentResponseDto;
import com.homework.homework01.dto.DeleteRequestDto;
import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.Comment;
import com.homework.homework01.entity.User;
import com.homework.homework01.entity.UserRoleEnum;
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
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        List<Comment> commentList = board.getCommentList();
        List<CommentResponseDto> list = new ArrayList<>();
        for (Comment comment : commentList) {
            list.add(new CommentResponseDto(comment));
        }

        return list;
    }

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {
        // id를 이용해서 게시글을 단건 조회한다.
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 조회된 게시글의 commentList에 추가한다.
        Comment comment = new Comment(board, requestDto, user);
        List<Comment> commentList = board.getCommentList();
        commentList.add(comment);

        // commentList를 BoardRepository에 저장한다.
        commentRepository.save(comment);
        return new CommentResponseDto(comment);


    }

    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findByIdAndBoard_Id(commentId, board.getId()).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        // 관리자만 게시글 수정 가능
        if (user.getRole() == UserRoleEnum.ADMIN) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("관리자만 게시글을 수정할 수 있습니다");
        }

    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findByIdAndBoard_Id(commentId, board.getId()).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        // 관리자만 댓글 삭제 가능
        if (user.getRole() == UserRoleEnum.ADMIN) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("관리자만 게시글을 삭제할 수 있습니다");
        }

    }
}
