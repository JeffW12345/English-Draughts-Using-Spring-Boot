package com.github.jeffw12345.draughts.models.game.move;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;
import com.github.jeffw12345.draughts.models.game.Square;

public class Move {
    private Player playerMakingMove;
    private Game game;
    private Square startingSquare;
    private Square endSquare;

    public int getChangeInRows(){
        return endSquare.getRowNumber() - startingSquare.getRowNumber();
    }
    public int getChangeInColumns(){
        return endSquare.getColumnNumber()- startingSquare.getColumnNumber();
    }
    private boolean isLegal = false; //TODO - Call method.
    private boolean convertsToKing = false; //TODO - Call method
}
