package com.example.board_spring3.user.repository;

import com.example.board_spring3.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <Users, Long> {
    Optional<Users> findByUsername(String username);

}
