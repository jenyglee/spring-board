package com.homework.homework01.controller;

import com.homework.homework01.dto.*;
import com.homework.homework01.entity.Board;
import com.homework.homework01.service.BoardService;
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
    @GetMapping("/board")
    public List<BoardResponseDto> getBoards(){
        return boardService.getBoards();
    }

    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request){
        return boardService.createBoard(requestDto, request);
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request){
        return boardService.updateBoard(id, requestDto, request);
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id, @RequestBody BoardDeleteRequestDto requestDto, HttpServletRequest request){
        boardService.deleteBoard(id, requestDto, request);
    }
}
