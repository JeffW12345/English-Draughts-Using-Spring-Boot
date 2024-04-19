package com.github.jeffw12345.draughts.game.models.move.type;

public interface MoveType {
    int getRowChange();
    int getColumnChange();

    boolean isOutOfBoundsForPieceAtPosition(int startRow, int startColumn);

    int getDestinationRowFromStartRow(int startRow);

    int getDestinationColumnFromStartColumn(int startColumn);
}
