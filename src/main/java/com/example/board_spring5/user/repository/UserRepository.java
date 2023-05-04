package com.example.board_spring5.user.repository;

import com.example.board_spring5.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <Users, Long> {
    Optional<Users> findByUsername(String username);

}
