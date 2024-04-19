package com.github.jeffw12345.draughts.game.models.move.type;

import lombok.Getter;

public enum KingMoveType implements MoveType {
    MOVE_ONE_UP_RIGHT(1, 1),
    JUMP_TWO_UP_RIGHT(2, 2),
    MOVE_ONE_UP_LEFT(1, -1),
    JUMP_TWO_UP_LEFT(2, -2),
    MOVE_ONE_DOWN_RIGHT(-1, 1),
    JUMP_TWO_DOWN_RIGHT(-2, 2),
    MOVE_ONE_DOWN_LEFT(-1, -1),
    JUMP_TWO_DOWN_LEFT(-2, -2);

    @Getter
    private final int rowChange;
    @Getter
    private final int columnChange;

    public synchronized boolean isOutOfBoundsForPieceAtPosition(int startRow, int startColumn) {
        int rowChange = getRowChange();
        int columnChange = getColumnChange();

        startRow += rowChange;
        startColumn += columnChange;

        return startRow < 0 || startRow > 7 || startColumn < 0 || startColumn > 7;
    }

    public synchronized int getDestinationRowFromStartRow(int startRow){
        return startRow + getRowChange();
    }

    public synchronized int getDestinationColumnFromStartColumn(int startColumn){
        return startColumn + getColumnChange();
    }

    KingMoveType(int rowChange, int columnChange){
        this.rowChange = rowChange;
        this.columnChange = columnChange;
    }
}

