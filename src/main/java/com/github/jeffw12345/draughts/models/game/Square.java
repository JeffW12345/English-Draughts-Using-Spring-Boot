package com.github.jeffw12345.draughts.models.game;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Square {

    //TODO - Does the square need its own co-ordinates?
    private SquareContent squareContent;
    private int rowNumber;
    private int columnNumber;
}
