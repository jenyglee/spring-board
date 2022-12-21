package com.homework.homework01.repository;

import com.homework.homework01.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndBoard_Id(Long id, Long boardId);
}
