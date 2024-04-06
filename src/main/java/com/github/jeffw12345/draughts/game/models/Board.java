package com.github.jeffw12345.draughts.game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jeffw12345.draughts.game.models.move.type.KingMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.MoveType;
import com.github.jeffw12345.draughts.game.models.move.type.RedManMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.WhiteManMoveType;
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

    @JsonProperty("empty_SQUARE")
    private final Square EMPTY_SQUARE = Square.builder().squareContent(SquareContent.EMPTY).build();

    @JsonProperty("white_MAN")
    private final Square WHITE_MAN = Square.builder().squareContent(SquareContent.WHITE_MAN).build();

    @JsonProperty("red_MAN")
    private final Square RED_MAN = Square.builder().squareContent(SquareContent.RED_MAN).build();


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
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 == 0 ? RED_MAN : EMPTY_SQUARE;
            }
            if (rowNumber == 1){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 != 0 ? RED_MAN : EMPTY_SQUARE;
            }
            if (rowNumber > 2 && rowNumber < 5){
                row.getSquaresOnRow()[columnNumber] = EMPTY_SQUARE;
            }
            if (rowNumber == 5 || rowNumber == 7){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 != 0 ? WHITE_MAN : EMPTY_SQUARE;
            }
            if (rowNumber == 6){
                row.getSquaresOnRow()[columnNumber] = columnNumber % 2 == 0 ? WHITE_MAN : EMPTY_SQUARE;
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
            case EMPTY:
                return 'e';
            default:
                return ' ';
        }
    }
    public static void main(String[] args){
        //TODO - Delete. For debugging
        Board board = new Board();
        System.out.println(board);
        SquareContent squareContent = board.getSquareContentAtRowAndColumn(0, 0);
        System.out.println(squareContent);
    }
}
