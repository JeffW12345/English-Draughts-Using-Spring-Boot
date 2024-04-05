package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;

public class MoveValidationService {

    public static boolean isMoveLegal(Game game, Move move){
        Colour colour = game.getColourOfPlayerMakingMove(move);

        if (!game.isTurnOfColour(colour)){
            return false;
        }
        SquareContent startingSquareContent = move.getStartSquare().getSquareContent();
        if (!isStartingSquareTheRightColour(colour, startingSquareContent)){
            return false;
        };

        SquareContent endSquareContent = move.getStartSquare().getSquareContent();
        if (endSquareContent != SquareContent.EMPTY){
            return false;
        }

        if (oneSquareDiagnonalMove(move)) {
            return isMovingInRightDirection(move, startingSquareContent);
        }

        if (!passesOvertakingMovesCheck(game, colour, move)) {
            return false;
        }

        return false;
    }

    private static boolean passesOvertakingMovesCheck(Game game, Colour colour, Move move) {
        Board board = game.getCurrentBoard();
        int startingRow = move.getStartSquareRow();
        int startingColumn = move.getStartSquareColumn();

        if (move.isLeftUpTwo() || move.isRightUpTwo()) {
            int intermediateRow = startingRow + 1;
            int intermediateColumn = move.isLeftUpTwo() ? startingColumn - 1 : startingColumn + 1;
            SquareContent intermediateSquare = board.getSquareContentAtRowAndColumn(intermediateRow, intermediateColumn);
            if (intermediateSquare == SquareContent.EMPTY) return true;
            if (isIntermediateSquareTheWrongColour(colour, intermediateSquare)) return true;
        }

        if (move.isLeftDownTwo() || move.isRightDownTwo()) {
            int intermediateRow = startingRow - 1;
            int intermediateColumn = move.isLeftDownTwo() ? startingColumn - 1 : startingColumn + 1;
            SquareContent intermediateSquare = board.getSquareContentAtRowAndColumn(intermediateRow, intermediateColumn);
            if (intermediateSquare == SquareContent.EMPTY) return true;
            return isIntermediateSquareTheWrongColour(colour, intermediateSquare);
        }
        return false;
    }
    private static boolean isMovingInRightDirection(Move move, SquareContent startingSquarecontent) {
        boolean movingUpBoardAsExpected = move.isMovingUpBoard() &&
                startingSquarecontent == SquareContent.RED_MAN;

        boolean movingDownBoardAsExpected = move.isMovingDownBoard() &&
                startingSquarecontent == SquareContent.WHITE_KING;

        return movingUpBoardAsExpected || movingDownBoardAsExpected;
    }
    private static boolean oneSquareDiagnonalMove(Move move) {
        return (move.isLeftUpOne() || move.isLeftDownOne() || move.isRightUpOne() || move.isRightDownOne());
    }

    private static boolean isStartingSquareTheRightColour(Colour startSquareColour,
                                                          SquareContent startSquareContent) {
        boolean redValid = startSquareColour == Colour.RED &&
                (startSquareContent == SquareContent.RED_KING ||
                        startSquareContent == SquareContent.RED_MAN);

        boolean whiteValid = startSquareColour == Colour.WHITE &&
                (startSquareContent == SquareContent.WHITE_KING ||
                        startSquareContent == SquareContent.WHITE_MAN);
        return redValid || whiteValid;
    }

    private static boolean isIntermediateSquareTheWrongColour(Colour startSquareColour,
                                                              SquareContent intermediateSquareContent) {
        boolean redValid = startSquareColour == Colour.RED &&
                (intermediateSquareContent == SquareContent.WHITE_KING ||
                        intermediateSquareContent == SquareContent.WHITE_MAN);

        boolean whiteValid = startSquareColour == Colour.WHITE &&
                (intermediateSquareContent == SquareContent.RED_KING ||
                        intermediateSquareContent == SquareContent.RED_MAN);
        return !redValid && !whiteValid;
    }
}