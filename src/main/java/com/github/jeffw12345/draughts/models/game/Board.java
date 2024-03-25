package com.github.jeffw12345.draughts.models.game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    Row[] listOfRows = new Row[8];

    public SquareContent getSquareContentAtRowAndColumn(int rowNumber, int columnNumber){
        return listOfRows[rowNumber].getSquareAtColumn(columnNumber).getSquareContent();
    }
}
