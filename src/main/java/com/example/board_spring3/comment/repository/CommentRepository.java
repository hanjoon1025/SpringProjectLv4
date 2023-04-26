package com.example.board_spring3.comment.repository;

import com.example.board_spring3.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
