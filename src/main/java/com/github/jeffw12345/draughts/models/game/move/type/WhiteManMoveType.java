package com.github.jeffw12345.draughts.models.game.move.type;

import lombok.Getter;

public enum WhiteManMoveType implements MoveType {
    MOVE_ONE_DOWN_RIGHT(-1, 1),
    JUMP_TWO_DOWN_RIGHT(-2, 2),
    MOVE_ONE_DOWN_LEFT(-1, -1),
    JUMP_TWO_DOWN_LEFT(-2, -2);

    @Getter
    private final int rowChange;
    @Getter
    private final int columnChange;

    WhiteManMoveType(int rowChange, int columnChange){
        this.rowChange = rowChange;
        this.columnChange = columnChange;
    }
}

