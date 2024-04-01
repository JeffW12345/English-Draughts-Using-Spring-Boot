package com.github.jeffw12345.draughts.models.game;

public enum SquareContent {
    EMPTY, WHITE_MAN, RED_MAN, WHITE_KING, RED_KING;

    public static Colour getColour(SquareContent squareContent){
        if (squareContent == SquareContent.RED_MAN || squareContent == SquareContent.RED_KING){
            return Colour.RED;
        }
        if (squareContent == SquareContent.WHITE_MAN || squareContent == SquareContent.WHITE_KING){
            return Colour.RED;
        }
        return null;
    }
}
