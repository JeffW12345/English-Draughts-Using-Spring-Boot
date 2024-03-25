package com.github.jeffw12345.draughts.models.game.move;

import lombok.Getter;

public enum RedManMoveType {
    MOVE_ONE_UP_RIGHT(1, 1),
    JUMP_TWO_UP_RIGHT(2, 2),
    MOVE_ONE_UP_LEFT(1, -1),
    JUMP_TWO_UP_LEFT(2, -2);
    @Getter
    private final int rowChange;
    @Getter
    private final int columnChange;

    RedManMoveType(int rowChange, int columnChange){
        this.rowChange = rowChange;
        this.columnChange = columnChange;
    }
}
