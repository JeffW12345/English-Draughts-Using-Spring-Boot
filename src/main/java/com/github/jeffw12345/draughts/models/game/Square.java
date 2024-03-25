package com.github.jeffw12345.draughts.models.game;

import lombok.Getter;

@Getter
public class Square {
    private SquareContent squareContent;
    private int rowNumber;
    private int columnNumber;
}
