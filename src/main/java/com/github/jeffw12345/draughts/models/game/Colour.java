package com.github.jeffw12345.draughts.models.game;

public enum Colour {
    RED, WHITE;

    public static SquareContent getKingSquareContent(Colour colour) {
        return colour == Colour.WHITE ? SquareContent.WHITE_KING : SquareContent.RED_KING;
    }

    public static SquareContent getManSquareContent(Colour colour) {
        return colour == Colour.WHITE ? SquareContent.WHITE_MAN : SquareContent.RED_MAN;
    }
}
