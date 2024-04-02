package com.github.jeffw12345.draughts.models.game;

public enum Colour {
    RED, WHITE, NONE;

    public static SquareContent getKingSquareContent(Colour colour) {
        return colour == Colour.WHITE ? SquareContent.WHITE_KING : SquareContent.RED_KING;
    }

    public static SquareContent getManSquareContent(Colour colour) {
        return colour == Colour.WHITE ? SquareContent.WHITE_MAN : SquareContent.RED_MAN;
    }

    public static Colour otherPlayerColour(Colour thisPlayerColour) {
        if (thisPlayerColour == Colour.WHITE) {
            return Colour.RED;
        }
        return Colour.WHITE;
    }
}
