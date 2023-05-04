package com.example.board_spring5.likes.repository;

import com.example.board_spring5.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByUsersIdAndBoardId(Long USER_ID, Long BOARD_ID);
    Optional<Likes> findByUsersIdAndBoardIdAndCommentId(Long USER_ID, Long BOARD_ID, Long COMMENT_ID);
    void deleteByUsersIdAndBoardId(Long USER_ID, Long BOARD_ID);
    void deleteByUsersIdAndBoardIdAndCommentId(Long USER_ID, Long BOARD_ID, Long COMMENT_ID);

    int countByBoardId(Long boardId);
    int countByCommentId(Long commentId);
}
