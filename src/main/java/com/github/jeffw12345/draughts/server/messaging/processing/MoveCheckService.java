package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.KingMoveType;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.move.MoveType;
import com.github.jeffw12345.draughts.models.game.move.RedManMoveType;
import com.github.jeffw12345.draughts.models.game.move.WhiteManMoveType;

public class MoveCheckService {

    public boolean isMoveLegal(Game game, Colour colour){
        Move move = game.getLatestMoveForColour(colour);
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

        if (!passesOvertakingMovesCheck(game, colour, move)) return false;

        return false;
    }

    private boolean passesOvertakingMovesCheck(Game game, Colour colour, Move move) {
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
    private boolean isMovingInRightDirection(Move move, SquareContent startingSquarecontent) {
        boolean movingUpBoardAsExpected = move.isMovingUpBoard() &&
                startingSquarecontent == SquareContent.RED_MAN;

        boolean movingDownBoardAsExpected = move.isMovingDownBoard() &&
                startingSquarecontent == SquareContent.WHITE_KING;

        return movingUpBoardAsExpected || movingDownBoardAsExpected;
    }
    private static boolean oneSquareDiagnonalMove(Move move) {
        return (move.isLeftUpOne() || move.isLeftDownOne() || move.isRightUpOne() || move.isRightDownOne());
    }

    private boolean isStartingSquareTheRightColour(Colour startSquareColour, SquareContent startSquareContent) {
        boolean redValid = startSquareColour == Colour.RED &&
                (startSquareContent == SquareContent.RED_KING || startSquareContent == SquareContent.RED_MAN);

        boolean whiteValid = startSquareColour == Colour.WHITE &&
                (startSquareContent == SquareContent.WHITE_KING || startSquareContent == SquareContent.WHITE_MAN);
        return redValid || whiteValid;
    }

    private boolean isIntermediateSquareTheWrongColour(Colour startSquareColour, SquareContent intermediateSquareContent) {
        boolean redValid = startSquareColour == Colour.RED &&
                (intermediateSquareContent == SquareContent.WHITE_KING || intermediateSquareContent == SquareContent.WHITE_MAN);

        boolean whiteValid = startSquareColour == Colour.WHITE &&
                (intermediateSquareContent == SquareContent.RED_KING || intermediateSquareContent == SquareContent.RED_MAN);
        return !redValid && !whiteValid;
    }
    public boolean anyLegalMovesForColour(Board board, Colour colour) {
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
                SquareContent content = board.getSquareContentAtRowAndColumn(rowIndex, columnIndex);

                boolean wrongColour = !content.toString().toLowerCase().contains(colour.toString().toLowerCase());
                if (content == SquareContent.EMPTY || wrongColour) {
                    continue;
                }

                MoveType[] moveTypes = null;
                if (content == SquareContent.WHITE_MAN) {
                    moveTypes = WhiteManMoveType.values();
                } else if (content == SquareContent.RED_MAN) {
                    moveTypes = RedManMoveType.values();
                } else if (content == SquareContent.WHITE_KING || content == SquareContent.RED_KING) {
                    moveTypes = KingMoveType.values();
                }

                if (moveTypes != null) {
                    for (MoveType moveType : moveTypes) {
                        int rowChange = moveType.getRowChange();
                        int columnChange = moveType.getColumnChange();
                        if (!outOfBounds(rowIndex, columnIndex, rowChange, columnChange)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private boolean outOfBounds(int row, int column, int rowChange, int columnChange){
            row -= rowChange;
            column -= columnChange;

            return row < 0 || row > 7 || column < 0 || column > 7;
        }
}