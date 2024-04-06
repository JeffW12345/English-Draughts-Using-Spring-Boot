package com.github.jeffw12345.draughts.game.models.move;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Square;
import com.github.jeffw12345.draughts.game.models.SquareContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Move {
    @JsonProperty("MOVE_ID")
    private String MOVE_ID = String.valueOf(UUID.randomUUID());
    @JsonProperty("startSquareColumn")
    private int startSquareColumn;
    @JsonProperty("startSquareRow")
    private int startSquareRow;
    @JsonProperty("startCoordinatesProvided")
    private boolean startCoordinatesProvided;
    @JsonProperty("endSquareColumn")
    private int endSquareColumn;
    @JsonProperty("endSquareRow")
    private int endSquareRow;
    @JsonProperty("endCoordinatesProvided")
    private boolean endCoordinatesProvided;

    @JsonProperty("startAndEndCoordinatesProvided")
    private boolean startAndEndCoordinatesProvided;

    @JsonProperty("overtakingMove")
    private boolean overtakingMove;
    @JsonProperty("oneSquareMove")
    private boolean oneSquareMove;

    @JsonProperty("moveStatus")
    private MoveStatus moveStatus = MoveStatus.PENDING;
    @JsonProperty("isTurnComplete")
    private boolean isTurnComplete;
    @JsonProperty("moveProcessedTimestamp")
    private Timestamp moveProcessedTimestamp;


    public void setStartCoordinates(int row, int column){
        startSquareColumn = column;
        startSquareRow = row;
        startCoordinatesProvided = true;
    }

    public void setEndCoordinates(int row, int column){
        endSquareColumn = column;
        endSquareRow = row;
        endCoordinatesProvided = true;
        oneSquareMove = isOneSquareMove();
        overtakingMove = isTwoSquareMove();
    }

    public boolean isOneSquareMove(){
        return Math.abs(startSquareColumn - endSquareColumn) == 1;
    }
    @JsonIgnore
    public boolean isTwoSquareMove(){
        return Math.abs(startSquareColumn - endSquareColumn) == 2;
    }

    @JsonIgnore
    public boolean isRightUpOne(){
        return (startSquareRow - endSquareRow == -1) && (startSquareColumn - endSquareColumn == -1);
    }
    @JsonIgnore
    public boolean isLeftUpOne() {
        return (startSquareRow - endSquareRow == 1) && (startSquareColumn - endSquareColumn == -1);
    }
    @JsonIgnore
    public boolean isRightDownOne() {
        return (startSquareRow - endSquareRow == -1) && (startSquareColumn - endSquareColumn == 1);
    }
    @JsonIgnore
    public boolean isLeftDownOne() {
        return (startSquareRow - endSquareRow == -1) && (startSquareColumn - endSquareColumn == -1);
    }
    @JsonIgnore
    public boolean isRightUpTwo() {
        return (startSquareRow - endSquareRow == -2) && (startSquareColumn - endSquareColumn == -2);
    }
    @JsonIgnore
    public boolean isLeftUpTwo() {
        return (startSquareRow - endSquareRow == 2) && (startSquareColumn - endSquareColumn == -2);
    }
    @JsonIgnore
    public boolean isRightDownTwo() {
        return (startSquareRow - endSquareRow == -2) && (startSquareColumn - endSquareColumn == 2);
    }
    @JsonIgnore
    public boolean isLeftDownTwo() {
        return (startSquareRow - endSquareRow == 2) && (startSquareColumn - endSquareColumn == 2);
    }
    @JsonIgnore
    public boolean isMovingUpBoard(){
        return endSquareRow > startSquareRow;
    }
    @JsonIgnore
    public boolean isMovingDownBoard(){
        return endSquareRow < startSquareRow;
    }
    @JsonIgnore
    public boolean startCoordinatesOnlyProvided(){
        return startCoordinatesProvided && !endCoordinatesProvided;
    }
    @JsonIgnore
    public boolean noStartOrEndSquareProvidedYet(){
        return !startCoordinatesProvided && !endCoordinatesProvided;
    }
    public boolean willMoveResultInCoronation(Board board){
        Colour playerColour = SquareContent
                .getColour(board.getSquareContentAtRowAndColumn(startSquareRow, startSquareColumn));
        return (playerColour == Colour.RED && endSquareRow == 7) || (playerColour == Colour.WHITE && endSquareRow == 0);
    }

    public void moveProcessedUpdate(MoveStatus newStatus){
        moveStatus = newStatus;
        moveProcessedTimestamp = new Timestamp(System.currentTimeMillis());
    }
}
