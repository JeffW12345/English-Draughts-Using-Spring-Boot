package com.github.jeffw12345.draughts.game.models;

public enum SquareContent {
    EMPTY,
    WHITE_MAN,
    RED_MAN,
    WHITE_KING,
    RED_KING;

    public static Colour getColour(SquareContent squareContent){
        if (squareContent == SquareContent.RED_MAN || squareContent == SquareContent.RED_KING){
            return Colour.RED;
        }
        if (squareContent == SquareContent.WHITE_MAN || squareContent == SquareContent.WHITE_KING){
            return Colour.WHITE;
        }
        return Colour.NONE;
    }

    public static boolean isPieceColourDifferentToColour(Colour colour, SquareContent squareContent){
        if (colour == Colour.WHITE && (squareContent == SquareContent.RED_MAN ||
                squareContent == SquareContent.RED_KING)){
            return true;
        }

        if (colour == Colour.RED && (squareContent == SquareContent.WHITE_MAN ||
                squareContent == SquareContent.WHITE_KING)){
            return true;
        }
        return false;
    }

    public static boolean canPieceTypeJumpUpwardsFromBottomOnly(SquareContent squareContent){
        return squareContent == SquareContent.RED_MAN;
    }

    public static boolean canPieceTypeJumpDownwardsFromTopOnly(SquareContent squareContent){
        return squareContent == SquareContent.WHITE_MAN;
    }

    public static boolean isAKing(SquareContent squareContent){
        return squareContent == SquareContent.RED_KING || squareContent == SquareContent.WHITE_KING;
    }


}
