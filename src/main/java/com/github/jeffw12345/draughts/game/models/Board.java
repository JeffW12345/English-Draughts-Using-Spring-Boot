package com.github.jeffw12345.draughts.game.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jeffw12345.draughts.game.models.move.Move;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Builder
@Setter
@Getter
public class Board {
    @JsonProperty("rows")
    private final BoardRow[] rows = new BoardRow[8];

    public Board() {
        setUpBoard();
    }

    private void setUpBoard() {
        for (int row = 0; row < 8; row++) {
            rows[row] = createRow(row);
        }
    }
    private BoardRow createRow(int rowNumber) {
        BoardRow row = new BoardRow();
        for (int columnNumber = 0; columnNumber < 8; columnNumber++) {
            if (rowNumber == 0 || rowNumber == 2){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 == 0
                        ? new Square(SquareContent.RED_MAN)
                        : new Square(SquareContent.EMPTY);
            }
            if (rowNumber == 1){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 != 0
                        ? new Square(SquareContent.RED_MAN)
                        : new Square(SquareContent.EMPTY);
            }
            if (rowNumber > 2 && rowNumber < 5){
                row.getSquaresOnRow()[columnNumber] = new Square(SquareContent.EMPTY);
            }
            if (rowNumber == 5 || rowNumber == 7){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 != 0
                        ? new Square(SquareContent.WHITE_MAN)
                        : new Square(SquareContent.EMPTY);
            }
            if (rowNumber == 6){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 == 0
                        ? new Square(SquareContent.WHITE_MAN)
                        : new Square(SquareContent.EMPTY);
            }
        }
        return row;
    }

    public SquareContent getSquareContentAtRowAndColumn(int rowNumber, int columnNumber) {
        if (isValidPosition(rowNumber, columnNumber)) {
            return rows[rowNumber].getSquareAtColumn(columnNumber).getSquareContent();
        } else {
            throw new IllegalArgumentException
                    (String.format("Invalid row or column index. Row: %s Column: %s", rowNumber, columnNumber));
        }
    }

    public Square getSquareAtRowAndColumn(int rowNumber, int columnNumber) {
        if (isValidPosition(rowNumber, columnNumber)) {
            return rows[rowNumber].getSquareAtColumn(columnNumber);
        } else {
            throw new IllegalArgumentException
                    (String.format("Invalid row or column index. Row: %s Column: %s", rowNumber, columnNumber));
        }
    }

    public void setSquareAtRowAndColumn(int rowNumber, int columnNumber, SquareContent requiredSquareContent){
        getSquareAtRowAndColumn(rowNumber, columnNumber).setSquareContent(requiredSquareContent);
    }

    private boolean isValidPosition(int rowNumber, int columnNumber) {
        return rowNumber >= 0 && rowNumber < 8 && columnNumber >= 0 && columnNumber < 8;
    }

    public void updateForCompletedMove(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("Invalid move: null");
        }

        Square startSquareOnBoard = getStartOfMoveSquare(move);
        Square destinationSquare = getMoveTerminationSquare(move);
        Square middleSquare = getIntermediateSquare(move);

        Colour colourOfPieceBeingMoved = SquareContent.getColour(startSquareOnBoard.getSquareContent());

        boolean isPieceAKing = startSquareOnBoard.containsAKing();

        startSquareOnBoard.setSquareContent(SquareContent.EMPTY);

        if (move.isOneSquareMove()) {
            updateForOneSquareMoveActions(move, destinationSquare, colourOfPieceBeingMoved, isPieceAKing);
        } else if (move.isOvertakingMove()) {
            updateForTwoSquareMoveActions(move, destinationSquare, middleSquare, colourOfPieceBeingMoved, isPieceAKing);
        }
    }

    private Square getStartOfMoveSquare(Move move) {
        return getSquareAtRowAndColumn(move.getStartSquareRow(), move.getStartSquareColumn());
    }

    private Square getIntermediateSquare(Move move) {
        int middleRow = (move.getStartSquareRow() + move.getEndSquareRow()) / 2;
        int middleColumn = (move.getStartSquareColumn() + move.getEndSquareColumn()) / 2;
        return getSquareAtRowAndColumn(middleRow, middleColumn);
    }

    public Square getMoveTerminationSquare(Move move) {
        return getSquareAtRowAndColumn(move.getEndSquareRow(), move.getEndSquareColumn());
    }

    private static void updateForTwoSquareMoveActions(Move move,
                                                      Square destinationSquare,
                                                      Square middleSquare,
                                                      Colour colourOfPieceBeingMoved,
                                                      boolean isPieceAKing) {
        if (move.willMoveResultInCoronation() || isPieceAKing){
            destinationSquare.setSquareContent(Colour.getKingSquareContentForColour(colourOfPieceBeingMoved));
        }else{
            destinationSquare.setSquareContent(Colour.getManSquareContentForColour(colourOfPieceBeingMoved));
        }
        middleSquare.setSquareContent(SquareContent.EMPTY);
    }

    private static void updateForOneSquareMoveActions(Move move,
                                                      Square destinationSquare,
                                                      Colour colourOfPieceBeingMoved,
                                                      boolean isPieceAKing) {
        if (move.willMoveResultInCoronation() || isPieceAKing) {
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = 7; row > -1; row--) {
            stringBuilder.append("Row number: ").append(row).append(" ");
            for (int column = 0; column < 8; column++) {
                SquareContent squareContent = rows[row].getSquaresOnRow()[column].getSquareContent();
                stringBuilder.append(getStringRepresentation(squareContent)).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private char getStringRepresentation(SquareContent squareContent) {
        switch (squareContent) {
            case RED_MAN:
                return 'r';
            case WHITE_MAN:
                return 'w';
            case RED_KING:
                return 'R';
            case WHITE_KING:
                return 'W';
            default:
                return 'e';
        }
    }
}
