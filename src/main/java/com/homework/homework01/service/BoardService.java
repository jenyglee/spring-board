package com.homework.homework01.service;

import com.homework.homework01.dto.*;
import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.User;
import com.homework.homework01.entity.UserRoleEnum;
import com.homework.homework01.jwtUtil.JwtUtil;
import com.homework.homework01.repository.BoardRepository;
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
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        List<BoardResponseDto> list = new ArrayList<>();
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();
        for (Board board : boards){
            list.add(new BoardResponseDto(board));
        }
        return list;

    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if(token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("Token Error");
            }

            // 유저인지 관리자인지 확인이 필요

            String subject = claims.getSubject();
            System.out.println("subject : "+ subject);
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = boardRepository.saveAndFlush(new Board(requestDto, user.getUsername()));
            return new BoardResponseDto(board);
        }
        return null;
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        board.validationPassword(requestDto.getPassword());
        if(token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("Token Error");
            }

            // 관리자만 게시글 수정 가능
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            if(user.getRole() == UserRoleEnum.ADMIN){
                board.update(requestDto);
                return new BoardResponseDto(board);
            }else{
                throw new IllegalArgumentException("관리자만 게시글을 수정할 수 있습니다");
            }
        }

        return null;
    }

    public void deleteBoard(Long id, BoardDeleteRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        board.validationPassword(requestDto.getPassword());
        if(token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("Token Error");
            }

            // 관리자만 게시글 삭제 가능
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            if(user.getRole() == UserRoleEnum.ADMIN){
                boardRepository.deleteById(id);
            }else{
                throw new IllegalArgumentException("관리자만 게시글을 삭제할 수 있습니다");
            }
        }
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );

        return new BoardResponseDto(board);
    }
}
