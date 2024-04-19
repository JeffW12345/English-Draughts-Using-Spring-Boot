package com.github.jeffw12345.draughts.game.models.move.type;

import lombok.Getter;

@Getter
public enum DownwardOvertakeJump implements MoveType {
    JUMP_TWO_DOWN_RIGHT(-2, 2),
    JUMP_TWO_DOWN_LEFT(-2, -2);
    private final int rowChange;
    private final int columnChange;

    public boolean isOutOfBoundsForPieceAtPosition(int startRow, int startColumn) {
        int rowChange = getRowChange();
        int columnChange = getColumnChange();

        startRow += rowChange;
        startColumn += columnChange;

        return startRow < 0 || startRow > 7 || startColumn < 0 || startColumn > 7;
    }

    public int getDestinationRowFromStartRow(int startRow){
        return startRow + getRowChange();
    }

    public int getDestinationColumnFromStartColumn(int startColumn){
        return startColumn + getColumnChange();
    }

    DownwardOvertakeJump(int rowChangeFromStartingSquare, int columnChangeFromStartingSquare){
        this.rowChange = rowChangeFromStartingSquare;
        this.columnChange = columnChangeFromStartingSquare;
    }
}

