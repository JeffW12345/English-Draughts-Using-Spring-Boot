package com.github.jeffw12345.draughts.game.models;

public enum Colour {
    RED, WHITE, NONE;

    public static SquareContent getKingSquareContentForColour(Colour colour) {
        return colour == Colour.WHITE ? SquareContent.WHITE_KING : SquareContent.RED_KING;
    }

    public static SquareContent getManSquareContentForColour(Colour colour) {
        return colour == Colour.WHITE ? SquareContent.WHITE_MAN : SquareContent.RED_MAN;
    }

    public static Colour getOtherPlayerColour(Colour thisPlayerColour) {
        if (thisPlayerColour == Colour.WHITE) {
            return Colour.RED;
        }
        return Colour.WHITE;
    }
}
