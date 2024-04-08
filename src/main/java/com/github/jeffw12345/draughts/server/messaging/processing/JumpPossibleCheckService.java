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

    public static boolean isOvertakePossibleForSquare(Colour colourOfPlayerPiece,
                                                      Board board,
                                                      SquareContent squareContent,
                                                      int row,
                                                      int column) {
        if (SquareContent.isAKing(squareContent)) {
            return canJumpFromSquare(board, UpwardOvertakeJump.class, squareContent, row, column, colourOfPlayerPiece) ||
                    canJumpFromSquare(board, DownwardOvertakeJump.class, squareContent, row, column, colourOfPlayerPiece);
        }
        if (SquareContent.canPieceTypeJumpDownwardsFromTopOnly(squareContent)) {
            return canJumpFromSquare(board, DownwardOvertakeJump.class, squareContent, row, column, colourOfPlayerPiece);
        }
        if (SquareContent.canPieceTypeJumpUpwardsFromBottomOnly(squareContent)) {
            return canJumpFromSquare(board, UpwardOvertakeJump.class, squareContent, row, column, colourOfPlayerPiece);
        }
        return false;
    }

    public static boolean canJumpFromSquare(Board board,
                                            Class<? extends Enum<? extends MoveType>> moveTypeEnumClass,
                                            SquareContent squareContent,
                                            int startRow,
                                            int startColumn,
                                            Colour colourOfPlayerPiece) {

        MoveType[] moveTypes = (MoveType[]) moveTypeEnumClass.getEnumConstants();

        for (MoveType moveType : moveTypes) {
            int endRow = moveType.getDestinationRowFromStartRow(startRow);
            int endColumn = moveType.getDestinationColumnFromStartColumn(startColumn);

            if (moveType.isOutOfBoundsForPieceAtPosition(endRow, endColumn)) {
                continue;
            }

            int rowChange = moveType.getRowChange();
            int columnChange = moveType.getColumnChange();

            int jumpedOverSquareRow = rowChange < 0
                    ? startRow + rowChange + 1
                    : startRow + rowChange -1;
            int jumpedOverSquareColumn = columnChange < 0
                    ? startColumn + columnChange + 1
                    : startColumn + columnChange -1;

            if (!isSquareOccupiedByOpponentPiece(jumpedOverSquareRow, jumpedOverSquareColumn, board, colourOfPlayerPiece)) {
                continue;
            }

            int destinationRow = moveType.getDestinationRowFromStartRow(startRow);
            int destinationColumn = moveType.getDestinationColumnFromStartColumn(startColumn);

            if (board.getSquareContentAtRowAndColumn(destinationRow, destinationColumn) == SquareContent.EMPTY) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSquareOccupiedByOpponentPiece(int row,
                                                          int column,
                                                          Board board,
                                                          Colour colourOfPlayerPiece) {

        SquareContent squareContent = board.getSquareContentAtRowAndColumn
                (row, column);

        return SquareContent.isPieceColourDifferentToColour(colourOfPlayerPiece, squareContent);
    }
}
