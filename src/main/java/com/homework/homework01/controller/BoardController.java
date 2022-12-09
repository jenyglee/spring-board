package com.homework.homework01.controller;

import com.homework.homework01.dto.BoardDeleteResponseDto;
import com.homework.homework01.dto.BoardRequestDto;
import com.homework.homework01.dto.BoardUpdateResponseDto;
import com.homework.homework01.entity.Board;
import com.homework.homework01.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    @GetMapping("/api/board")
    public List<Board> getBoards(){
        return boardService.getBoards();
    }

    @GetMapping("/api/board/{id}")
    public Board getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PostMapping("/api/board")
    public Board createBoard(@RequestBody BoardRequestDto requestDto){
        return boardService.createBoard(requestDto);
    }

    @PutMapping("/api/board/{id}")
    public BoardUpdateResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        return boardService.updateBoard(id, requestDto);
    }

    @DeleteMapping("/api/board/{id}")
    public BoardDeleteResponseDto deleteBoard(@PathVariable Long id, @RequestBody Map<String, String> password){
        return boardService.deleteBoard(id, password);
    }
}
