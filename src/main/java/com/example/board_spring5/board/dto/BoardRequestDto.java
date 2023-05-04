package com.example.board_spring5.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // generates getters for the fields in the class automatically
@NoArgsConstructor // generates a no argument constructor for the class automatically
public class BoardRequestDto { // Board Requesting Data to the client

    private String title; // Clients need to write title
    private String content; // Clients need to write content

}
