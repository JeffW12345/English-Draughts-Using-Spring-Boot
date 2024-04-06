package com.github.jeffw12345.draughts.game.models.move.type;

import com.github.jeffw12345.draughts.game.models.move.Move;

public interface MoveType {
    int getRowChange();
    int getColumnChange();

    default boolean isOutOfBoundsForPieceAtPosition(int startRow, int startColumn) {
        int rowChange = getRowChange();
        int columnChange = getColumnChange();

        startRow -= rowChange;
        startColumn -= columnChange;

        return startRow < 0 || startRow > 7 || startColumn < 0 || startColumn > 7;
    }

    default int getDestinationRow(int startRow){
        return startRow + getRowChange();
    }

    default int getDestinationColumn(int startColumn){
        return startColumn + getColumnChange();
    }
}
