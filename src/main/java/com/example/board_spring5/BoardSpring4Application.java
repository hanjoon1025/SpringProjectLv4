package com.example.board_spring5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoardSpring4Application {

    public static void main(String[] args) {
        SpringApplication.run(BoardSpring4Application.class, args);
    }

}
