package com.github.jeffw12345.draughts.game.models.move.type;

import lombok.Getter;

@Getter
public enum BackwardJump implements MoveType {
    JUMP_TWO_DOWN_RIGHT(-2, 2),
    JUMP_TWO_DOWN_LEFT(-2, -2);
    private final int rowChange;
    private final int columnChange;

    BackwardJump(int rowChangeFromStartingSquare, int columnChangeFromStartingSquare){
        this.rowChange = rowChangeFromStartingSquare;
        this.columnChange = columnChangeFromStartingSquare;
    }
}

