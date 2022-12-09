package com.homework.homework01.service;

import com.homework.homework01.dto.BoardDeleteResponseDto;
import com.homework.homework01.dto.BoardRequestDto;
import com.homework.homework01.dto.BoardUpdateResponseDto;
import com.homework.homework01.entity.Board;
import com.homework.homework01.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    @Transactional(readOnly = true)
    public List<Board> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc();
    }

    public Board createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);
        boardRepository.save(board);
        return board;
    }

    @Transactional
    public BoardUpdateResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isEmpty()){
            return new BoardUpdateResponseDto(false, "없는 게시물입니다.");
        }

        if (board.get().validationPassword(requestDto.getPassword())) {
            board.get().update(requestDto);
            return new BoardUpdateResponseDto(board.get(), true, "수정되었습니다.");
        }
        return new BoardUpdateResponseDto(false, "비밀번호가 틀렸습니다.");
    }

    public BoardDeleteResponseDto deleteBoard(Long id, Map<String, String> password) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isEmpty()){
            return new BoardDeleteResponseDto(false, "없는 게시물입니다.");
        }
        if(board.get().validationPassword(password.get("password"))){
            boardRepository.deleteById(id);
            return new BoardDeleteResponseDto(true, "삭제되었습니다.");
        }
        return new BoardDeleteResponseDto(false, "비밀번호가 틀렸습니다.");
    }

    public Board getBoard(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isEmpty()){
            return null;
        }
        return board.get();
    }
}
