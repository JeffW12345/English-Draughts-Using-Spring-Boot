package com.github.jeffw12345.draughts.game.models.move.type;

import lombok.Getter;

public enum RedManMoveType implements MoveType {
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