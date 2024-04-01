package com.github.jeffw12345.draughts.models.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Board {
    private final Row[] rows = new Row[8];
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
    private Row createRow(int rowNumber) {
        Row row = new Row();
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
}
