package com.homework.homework01.repository;

import com.homework.homework01.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtDesc();
    Optional<Board> findByUsername(String username);
}
