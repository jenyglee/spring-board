package com.homework.homework01.repository;

import com.homework.homework01.entity.Board;
import com.homework.homework01.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);
    Optional<Comment> findByIdAndBoardId(Long commentId, Long boardId);
}
