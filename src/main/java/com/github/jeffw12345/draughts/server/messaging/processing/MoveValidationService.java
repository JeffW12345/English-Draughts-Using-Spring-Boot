package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MoveValidationService {

    public static boolean isMoveLegal(Game game, Move move) {
        Colour playerMakingMoveColour = game.getColourOfPlayerMakingMove(move);

        if (!game.isTurnOfColour(playerMakingMoveColour)) {
            return false;
        }

        Board board = game.getCurrentBoard();
        int startRow = move.getStartSquareRow();
        int startColumn = move.getStartSquareColumn();
        SquareContent startSquareContent = board.getSquareContentAtRowAndColumn(startRow, startColumn);

        if (!isStartingSquareValid(playerMakingMoveColour, startSquareContent)) {
            return false;
        }

        int endRow = move.getEndSquareRow();
        int endColumn = move.getEndSquareColumn();
        SquareContent endSquareContent = board.getSquareContentAtRowAndColumn(endRow, endColumn);
        if (endSquareContent != SquareContent.EMPTY) {
            return false;
        }

        if (!isMovingInRightDirection(move, startSquareContent)) {
            return false;
        }

        if (move.isOvertakingMove() && !isOverTakeValid(game, playerMakingMoveColour, move)){
            return false;
        }

        return true;
    }

    private static boolean isOverTakeValid(Game game, Colour startingSquareColour, Move move) {
        Board board = game.getCurrentBoard();
        int startRow = move.getStartSquareRow();
        int startColumn = move.getStartSquareColumn();
        int intermediateRow = (startRow + move.getEndSquareRow()) / 2;
        int intermediateColumn = (startColumn + move.getEndSquareColumn()) / 2;
        SquareContent intermediateSquareContent = board.getSquareContentAtRowAndColumn(intermediateRow, intermediateColumn);
        Colour intermediateSquareColour = SquareContent.getColour(intermediateSquareContent);
        return intermediateSquareColour != startingSquareColour || intermediateSquareColour != Colour.NONE;
    }

    private static boolean isStartingSquareValid(Colour startSquareColour, SquareContent startSquareContent) {
        Colour expectedColour = SquareContent.getColour(startSquareContent);
        return expectedColour == startSquareColour && expectedColour != Colour.NONE;
    }

    private static boolean isMovingInRightDirection(Move move, SquareContent startingSquareContent) {
        if (startingSquareContent == SquareContent.RED_MAN) {
            return move.isMovingUpBoard();
        } else if (startingSquareContent == SquareContent.WHITE_MAN) {
            return move.isMovingDownBoard();
        }
        return true;
    }
}
