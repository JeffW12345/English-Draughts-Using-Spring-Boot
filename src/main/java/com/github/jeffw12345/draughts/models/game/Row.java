package com.github.jeffw12345.draughts.models.game;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private Square[] squaresOnRow = new Square[8];

    public Square getSquareAtColumn(int column){
        return squaresOnRow[column];
    }


}
