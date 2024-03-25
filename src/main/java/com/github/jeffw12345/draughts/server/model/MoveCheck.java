package com.github.jeffw12345.draughts.server.model;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;

public class MoveCheck {

    public boolean isMoveLegal(Move move, Board board){
        // TODO - Iterate through emums. Do any of them represent this move?
        // If so, do other checks.
        return false;
    }

    public boolean anyLegalMovesForColour(Board board, Colour colour){
        // TODO - For square in board.
        // If not right colour or empty, continue
        // Iterate through enums for piece type.
        // If a move would be out of bounds, break.
        // If free square in direction of allowed movement, return true.
        // If opponent in direction of allowed movement and square to jump available, return true.
        // Else return false
        return false;
    }

    private boolean outOfBounds(int row, int column){
        return row < 0 || row > 7 || column < 0 || column > 7;
    }
}
