package com.github.jeffw12345.draughts.models.game.move;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;
import com.github.jeffw12345.draughts.models.game.Square;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
public class Move {
    private String MOVE_ID = String.valueOf(UUID.randomUUID());
    private Player playerMakingMove;
    private Game game;
    private Square startSquare;
    private Square endSquare;

    private int startSquareColumn;
    private int startSquareRow;
    private boolean startCoordinatesProvided;

    private int endSquareColumn;
    private int endSquareRow;
    private boolean endCoordinatesProvided;

    public int getChangeInRows(){
        return endSquare.getRowNumber() - startSquare.getRowNumber();
    }
    public int getChangeInColumns(){
        return endSquare.getColumnNumber()- startSquare.getColumnNumber();
    }
    private boolean isLegal = false; //TODO - Call method.
    private boolean convertsToKing = false; //TODO - Call method

    public void setStartCoordinates(int column, int row){
        startSquareColumn = column;
        startSquareRow = row;
        startCoordinatesProvided = true;
    }

    public void setEndCoordinates(int column, int row){
        endSquareColumn = column;
        endSquareRow = row;
        endCoordinatesProvided = true;
    }

    public boolean startCoordinatesOnlyProvided(){
        return startCoordinatesProvided && !endCoordinatesProvided;
    }
    public boolean noCoordinatesOnlyProvided(){
        return !startCoordinatesProvided && !endCoordinatesProvided;
    }
}
