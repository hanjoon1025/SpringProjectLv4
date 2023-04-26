package com.example.board_spring3.board.repository;

import com.example.board_spring3.board.entity.Board;
import com.example.board_spring3.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Users> findByUsername(String username);
}
