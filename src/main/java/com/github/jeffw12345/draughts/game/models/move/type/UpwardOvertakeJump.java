package com.github.jeffw12345.draughts.game.models.move.type;

import lombok.Getter;

public enum UpwardOvertakeJump implements MoveType {
    JUMP_TWO_UP_RIGHT(2, 2),
    JUMP_TWO_UP_LEFT(2, -2);
    @Getter
    private final int rowChange;
    @Getter
    private final int columnChange;

    UpwardOvertakeJump(int rowChange, int columnChange){
        this.rowChange = rowChange;
        this.columnChange = columnChange;
    }
}
