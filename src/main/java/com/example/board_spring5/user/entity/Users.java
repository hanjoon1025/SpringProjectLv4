package com.example.board_spring5.user.entity;

import com.example.board_spring5.user.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name="users")
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public Users(UserRequestDto userRequestDto, UserRoleEnum role) {
        this.username = userRequestDto.getUsername();
        this.password = userRequestDto.getPassword();
        this.role = role;
    }
}
