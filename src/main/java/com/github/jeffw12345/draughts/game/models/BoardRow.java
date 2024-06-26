package com.github.jeffw12345.draughts.game.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRow {
    private Square[] squaresOnRow = new Square[8];
    public Square getSquareAtColumn(int column){
        return squaresOnRow[column];
    }
}
