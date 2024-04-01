package com.github.jeffw12345.draughts.models.game.move;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;
import com.github.jeffw12345.draughts.models.game.Square;
import com.github.jeffw12345.draughts.server.messaging.processing.MoveValidationService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
public class Move {
    private String MOVE_ID = String.valueOf(UUID.randomUUID());
    private Square startSquare;
    private Square endSquare;

    private int startSquareColumn;
    private int startSquareRow;
    private boolean startCoordinatesProvided;

    private int endSquareColumn;
    private int endSquareRow;
    private boolean endCoordinatesProvided;
    private boolean isLegal = false; //TODO - Call method.
    private boolean convertsToKing = false; //TODO - Call method

    private boolean hasMoveBeenVerified;
    private boolean isMoveLegal;

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

    public boolean isOneSquareMove(){
        return Math.abs(startSquareColumn - endSquareColumn) == 1;
    }

    public boolean isTwoSquareMove(){
        return Math.abs(startSquareColumn - endSquareColumn) == 1;
    }

    public int absoluteVerticalDistance(){
        return Math.abs(startSquareRow - endSquareRow);
    }

    public boolean isRightUpOne(){
        return (startSquareRow - endSquareRow == -1) && (startSquareColumn - endSquareColumn == -1);
    }
    public boolean isLeftUpOne() {
        return (startSquareRow - endSquareRow == 1) && (startSquareColumn - endSquareColumn == -1);
    }
    public boolean isRightDownOne() {
        return (startSquareRow - endSquareRow == -1) && (startSquareColumn - endSquareColumn == 1);
    }
    public boolean isLeftDownOne() {
        return (startSquareRow - endSquareRow == -1) && (startSquareColumn - endSquareColumn == -1);
    }
    public boolean isRightUpTwo() {
        return (startSquareRow - endSquareRow == -2) && (startSquareColumn - endSquareColumn == -2);
    }
    public boolean isLeftUpTwo() {
        return (startSquareRow - endSquareRow == 2) && (startSquareColumn - endSquareColumn == -2);
    }
    public boolean isRightDownTwo() {
        return (startSquareRow - endSquareRow == -2) && (startSquareColumn - endSquareColumn == 2);
    }
    public boolean isLeftDownTwo() {
        return (startSquareRow - endSquareRow == 2) && (startSquareColumn - endSquareColumn == 2);
    }
    public boolean isMovingUpBoard(){
        return endSquareRow > startSquareRow;
    }
    public boolean isMovingDownBoard(){
        return endSquareRow > startSquareRow;
    }

    public boolean startCoordinatesOnlyProvided(){
        return startCoordinatesProvided && !endCoordinatesProvided;
    }
    public boolean noCoordinatesOnlyProvided(){
        return !startCoordinatesProvided && !endCoordinatesProvided;
    }

    public boolean willMoveResultInCoronation(Game game){
        Colour playerColour = game.getColourOfPlayerMakingMove(this);
        return (playerColour == Colour.RED && endSquareRow == 7) || (playerColour == Colour.WHITE && endSquareRow == 0);
    }
    public Square getStartOfMoveSquare(Board board){
        return board.getSquareAtRowAndColumn(startSquareRow, startSquareColumn);
    }
    public Square getMoveTerminationSquare(Board board){
        return board.getSquareAtRowAndColumn(endSquareRow, endSquareColumn);
    }

    public Square getIntermediateSquare(Board board){
        int middleRow = (startSquareRow + endSquareRow) / 2;
        int middleColumn = (startSquareColumn + endSquareColumn) / 2;
        return board.getSquareAtRowAndColumn(middleRow, middleColumn);
    }
    public boolean isLegal(Game game){
        return MoveValidationService.isMoveLegal(game, this);
    }
}
