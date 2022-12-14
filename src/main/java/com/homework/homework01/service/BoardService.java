package com.homework.homework01.service;

import com.homework.homework01.dto.*;
import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.User;
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
    public List<BoardResponseDto> getBoards(HttpServletRequest request) {
        // String token = jwtUtil.resolveToken(request);
        // Claims claims;
        List<BoardResponseDto> list = new ArrayList<>();
        // if(token != null){
        //     if(jwtUtil.validateToken(token)){
        //         claims = jwtUtil.getUserInfoFromToken(token);
        //     }else{
        //         throw new IllegalArgumentException("Token Error");
        //     }
        //     List<Board> boards = boardRepository.findByUsername();
        //     for (Board board : boards){
        //         list.add(new BoardResponseDto(board));
        //     }
        //     return list;
        // }
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
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        board.validationPassword(requestDto.getPassword());
        if(token != null){
            if(jwtUtil.validateToken(token)){
                board.update(requestDto);
                return new BoardResponseDto(board);
            }else {
                throw new IllegalArgumentException("Token Error");
            }
        }

        return null;
    }

    public void deleteBoard(Long id, BoardDeleteRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("없는 게시물입니다.")
        );
        board.validationPassword(requestDto.getPassword());
        if(token != null){
            if(jwtUtil.validateToken(token)){
                boardRepository.deleteById(id);
            }else{
                throw new IllegalArgumentException("Token Error");
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
