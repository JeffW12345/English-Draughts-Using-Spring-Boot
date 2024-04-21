package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.type.DownwardOvertakeJump;
import com.github.jeffw12345.draughts.game.models.move.type.UpwardOvertakeJump;
import com.github.jeffw12345.draughts.game.models.move.type.MoveType;

public class JumpPossibleCheckService {

    public boolean isJumpPossibleForColour(Colour colourOfPlayerPiece, Board board){
        for (int row = 0; row < 8; row ++){
            for (int column = 0; column < 8; column++){
                SquareContent squareContent = board.getSquareContentAtRowAndColumn(row, column);
                if (SquareContent.getColour(squareContent) != colourOfPlayerPiece){
                    continue;
                }
                if (isOvertakePossibleForSquare(colourOfPlayerPiece, board, squareContent, row, column)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOvertakePossibleForSquare(Colour colourOfPlayerPiece,
                                                      Board board,
                                                      SquareContent squareContent,
                                                      int row,
                                                      int column) {
        if (SquareContent.isAKing(squareContent)) {
            return canJumpFromSquare(board, UpwardOvertakeJump.class, row, column, colourOfPlayerPiece) ||
                    canJumpFromSquare(board, DownwardOvertakeJump.class, row, column, colourOfPlayerPiece);
        }
        if (SquareContent.canPieceTypeJumpDownwardsFromTopOnly(squareContent)) {
            return canJumpFromSquare(board, DownwardOvertakeJump.class, row, column, colourOfPlayerPiece);
        }
        if (SquareContent.canPieceTypeJumpUpwardsFromBottomOnly(squareContent)) {
            return canJumpFromSquare(board, UpwardOvertakeJump.class, row, column, colourOfPlayerPiece);
        }
        return false;
    }

    private static boolean canJumpFromSquare(Board board,
                                            Class<? extends Enum<? extends MoveType>> moveTypeEnumClass,
                                            int startOfNewMoveRow,
                                            int startOfNewMoveColumn,
                                            Colour colourOfPlayerPiece) {

        MoveType[] moveTypes = (MoveType[]) moveTypeEnumClass.getEnumConstants();

        for (MoveType moveType : moveTypes) {
            if (moveType.isOutOfBoundsForPieceAtPosition(startOfNewMoveRow, startOfNewMoveColumn)) {
                continue;
            }

            int rowChange = moveType.getRowChange();
            int columnChange = moveType.getColumnChange();

            int jumpedOverSquareRow = rowChange < 0
                    ? startOfNewMoveRow + rowChange + 1
                    : startOfNewMoveRow + rowChange -1;
            int jumpedOverSquareColumn = columnChange < 0
                    ? startOfNewMoveColumn + columnChange + 1
                    : startOfNewMoveColumn + columnChange -1;

            if (!isSquareOccupiedByOpponentPiece(jumpedOverSquareRow, jumpedOverSquareColumn, board, colourOfPlayerPiece)) {
                continue;
            }

            int destinationRow = moveType.getDestinationRowFromStartRow(startOfNewMoveRow);
            int destinationColumn = moveType.getDestinationColumnFromStartColumn(startOfNewMoveColumn);

            if (board.getSquareContentAtRowAndColumn(destinationRow, destinationColumn) == SquareContent.EMPTY) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSquareOccupiedByOpponentPiece(int row,
                                                          int column,
                                                          Board board,
                                                          Colour colourOfPlayerPiece) {

        SquareContent squareContent = board.getSquareContentAtRowAndColumn
                (row, column);

        return SquareContent.isPieceColourDifferentToColour(colourOfPlayerPiece, squareContent);
    }
}
