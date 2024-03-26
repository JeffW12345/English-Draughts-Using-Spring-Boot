package com.github.jeffw12345.draughts.models.game;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Row {
    private Square[] squaresOnRow = new Square[8];

    public Square getSquareAtColumn(int column){
        return squaresOnRow[column];
    }


}
