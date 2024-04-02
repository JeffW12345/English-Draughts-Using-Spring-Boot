package com.github.jeffw12345.draughts.models.game;

import com.github.jeffw12345.draughts.models.game.move.Move;
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
            // TODO - Code to exit gracefully
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    public Square getSquareAtRowAndColumn(int rowNumber, int columnNumber) {
        if (isValidPosition(rowNumber, columnNumber)) {
            return rows[rowNumber].getSquareAtColumn(columnNumber);
        } else {
            // TODO - Code to exit gracefully
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    private boolean isValidPosition(int rowNumber, int columnNumber) {
        return rowNumber >= 0 && rowNumber < 8 && columnNumber >= 0 && columnNumber < 8;
    }

    public void updateForCompletedMove(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("Invalid move: null");
            // TODO - Code to exit gracefully
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
            destinationSquare.setSquareContent(Colour.getKingSquareContent(colourOfPieceBeingMoved));
        }else{
            destinationSquare.setSquareContent(Colour.getManSquareContent(colourOfPieceBeingMoved));
        }
        middleSquare.setSquareContent(SquareContent.EMPTY);
    }

    private static void updateForOneSquareMoveActions(Move move,
                                                      Square destinationSquare,
                                                      Colour colourOfPieceBeingMoved,
                                                      Board board) {
        if (move.willMoveResultInCoronation(board)) {
            destinationSquare.setSquareContent(Colour.getKingSquareContent(colourOfPieceBeingMoved));
        } else {
            destinationSquare.setSquareContent(Colour.getManSquareContent(colourOfPieceBeingMoved));
        }
    }
}
