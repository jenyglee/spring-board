package com.homework.homework01.service;

import com.homework.homework01.dto.*;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        List<BoardResponseDto> list = new ArrayList<>();
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();
        for (Board board : boards){
            List<Comment> comments = commentRepository.findAllByBoardId(board.getId());
            list.add(new BoardResponseDto(board, comments));
        }

        return list;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = boardRepository.saveAndFlush(new Board(requestDto, user.getUsername()));
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto,User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        if(user.getRole() == UserRoleEnum.ADMIN){
            board.update(requestDto);
            return new BoardResponseDto(board);
        }else{
            throw new IllegalArgumentException("관리자만 게시글을 수정할 수 있습니다");
        }
    }

    public void deleteBoard(Long id,User user) {
        // 관리자만 게시글 삭제 가능
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        if(user.getRole() == UserRoleEnum.ADMIN){
            boardRepository.deleteById(board.getId());
        }else{
            throw new IllegalArgumentException("관리자만 게시글을 삭제할 수 있습니다");
        }

    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        List<Comment> comments = commentRepository.findAllByBoardId(id);


        return new BoardResponseDto(board, comments);
    }
}
