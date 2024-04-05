package com.github.jeffw12345.draughts.game.models;

import com.github.jeffw12345.draughts.game.models.move.type.KingMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.MoveType;
import com.github.jeffw12345.draughts.game.models.move.type.RedManMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.WhiteManMoveType;
import com.github.jeffw12345.draughts.game.models.move.Move;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Board {
    private final BoardRow[] rows = new BoardRow[8];
    private final Square EMPTY_SQUARE = Square.builder().squareContent(SquareContent.EMPTY).build();
    private final Square WHITE_MAN = Square.builder().squareContent(SquareContent.WHITE_MAN).build();
    private final Square RED_MAN = Square.builder().squareContent(SquareContent.RED_MAN).build();

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            rows[row] = createRow(row);
        }
    }
    private BoardRow createRow(int rowNumber) {
        BoardRow row = new BoardRow();
        for (int column = 0; column < 8; column++) {
            if ((rowNumber + column) % 2 == 0) {
                row.getSquaresOnRow()[column] = EMPTY_SQUARE;
            } else if (rowNumber < 3) {
                row.getSquaresOnRow()[column] = WHITE_MAN;
            } else if (rowNumber > 4) {
                row.getSquaresOnRow()[column] = RED_MAN;
            } else {
                row.getSquaresOnRow()[column] = EMPTY_SQUARE;
            }
        }
        return row;
    }

    public SquareContent getSquareContentAtRowAndColumn(int rowNumber, int columnNumber) {
        if (isValidPosition(rowNumber, columnNumber)) {
            return rows[rowNumber].getSquareAtColumn(columnNumber).getSquareContent();
        } else {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    public Square getSquareAtRowAndColumn(int rowNumber, int columnNumber) {
        if (isValidPosition(rowNumber, columnNumber)) {
            return rows[rowNumber].getSquareAtColumn(columnNumber);
        } else {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    private boolean isValidPosition(int rowNumber, int columnNumber) {
        return rowNumber >= 0 && rowNumber < 8 && columnNumber >= 0 && columnNumber < 8;
    }

    public void updateForCompletedMove(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("Invalid move: null");
        }

        Square startSquareOnBoard = move.getStartOfMoveSquare(this);
        Square destinationSquare = move.getMoveTerminationSquare(this);
        Square middleSquare = move.getIntermediateSquare(this);
        Colour colourOfPieceBeingMoved = SquareContent.getColour(startSquareOnBoard.getSquareContent());

        startSquareOnBoard.setSquareContent(SquareContent.EMPTY);

        if (move.isOneSquareMove()) {
            updateForOneSquareMoveActions(move, destinationSquare, colourOfPieceBeingMoved, this);
        } else if (move.isOvertakingMove()) {
            updateForTwoSquareMoveActions(move, destinationSquare, middleSquare, colourOfPieceBeingMoved, this);
        }
    }

    private static void updateForTwoSquareMoveActions(Move move,
                                                      Square destinationSquare,
                                                      Square middleSquare,
                                                      Colour colourOfPieceBeingMoved,
                                                      Board board) {
        if (move.willMoveResultInCoronation(board)){
            destinationSquare.setSquareContent(Colour.getKingSquareContentForColour(colourOfPieceBeingMoved));
        }else{
            destinationSquare.setSquareContent(Colour.getManSquareContentForColour(colourOfPieceBeingMoved));
        }
        middleSquare.setSquareContent(SquareContent.EMPTY);
    }

    private static void updateForOneSquareMoveActions(Move move,
                                                      Square destinationSquare,
                                                      Colour colourOfPieceBeingMoved,
                                                      Board board) {
        if (move.willMoveResultInCoronation(board)) {
            destinationSquare.setSquareContent(Colour.getKingSquareContentForColour(colourOfPieceBeingMoved));
        } else {
            destinationSquare.setSquareContent(Colour.getManSquareContentForColour(colourOfPieceBeingMoved));
        }
    }

    public boolean hasNoSquaresOfColour(Colour colourToCheck) {
        for (int row = 0; row < 8; row++){
            for (int column = 0; column < 8; column++){
                SquareContent squareContent = getSquareContentAtRowAndColumn(row, column);
                if (SquareContent.getColour(squareContent) == colourToCheck){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasNoLegalMovesForColour(Colour colour) {
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
                SquareContent content = getSquareContentAtRowAndColumn(rowIndex, columnIndex);

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
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean jumpPossibleForMoveType(SquareContent startingSquareContent, Move move,
                                           Class<? extends Enum<? extends MoveType>> moveTypeEnumClass) {
        MoveType[] moveTypes = (MoveType[]) moveTypeEnumClass.getEnumConstants();
        for (MoveType moveType : moveTypes) {
            int startRow = move.getEndSquareRow();
            int startColumn = move.getEndSquareColumn();
            int rowChange = moveType.getRowChange();
            int columnChange = moveType.getColumnChange();
            if (!outOfBounds(startRow, startColumn, rowChange, columnChange)) {
                int jumpedSquareRow = rowChange < 0
                        ? startRow + rowChange + 1
                        : startRow + rowChange -1;
                int jumpedSquareColumn = columnChange < 0
                        ? startColumn + columnChange + 1
                        : startColumn + columnChange -1;

                SquareContent middleSquareContent = getSquareContentAtRowAndColumn(jumpedSquareRow, jumpedSquareColumn);
                Colour startingSquare = SquareContent.getColour(startingSquareContent);
                Colour opponentColour = Colour.getOtherPlayerColour(startingSquare);
                if(SquareContent.getColour(middleSquareContent) == opponentColour){
                    return true;
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

    //TODO - Do toString() of Board for testing.
}
