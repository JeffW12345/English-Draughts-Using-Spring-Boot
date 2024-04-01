package com.github.jeffw12345.draughts.models.game;

import com.github.jeffw12345.draughts.models.game.move.Move;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import java.util.LinkedList;

@Setter
@Getter
public class Game {
    private String gameId = String.valueOf(UUID.randomUUID());
    private Board currentBoard;
    private Player redPlayer;
    private Player whitePlayer;
    private boolean isRedTurn;
    private GameStatus gameStatus = GameStatus.AWAITING_NEW_GAME;
    private static LinkedList<Move> ConcurrentLinkedDeque;
    private static final ConcurrentLinkedDeque<Move>  redPlayerMoves = new ConcurrentLinkedDeque<>();
    private static final ConcurrentLinkedDeque<Move>  whitePlayerMoves = new ConcurrentLinkedDeque<>();

    public boolean awaitingNewGame(){
        return gameStatus == GameStatus.AWAITING_NEW_GAME;
    }

    public void addPlayer(Player player){
        if(whitePlayer != null && redPlayer != null) return; //TODO - Throw exception
        if(redPlayer == null){
            redPlayer = player;
        }
        else{
            whitePlayer = player;
        }
    }

    public void addMove(Move move, Colour colour){
        if (colour == Colour.RED){
            redPlayerMoves.push(move);
        }
        else{
            whitePlayerMoves.push(move);
        }
    }

    public boolean isTurnOfColour(Colour colour){
        return (colour==Colour.RED && isRedTurn) || (colour==Colour.WHITE && !isRedTurn);
    }

    public Move getLatestMoveForColour(Colour colour){
        if (colour == Colour.RED){
            return redPlayerMoves.peek();
        }
        else{
            return whitePlayerMoves.peek();
        }
    }

    public void changeStatusToInProgress(){
        gameStatus = GameStatus.IN_PROGRESS;
    }

    public void newGamePlayerNotificationActions() {
        redPlayer.newGameClientNotifications(this);
        whitePlayer.newGameClientNotifications(this);
    }

    public void changeTurns(){
        // TODO - Check how much of this is necessary
        isRedTurn = !isRedTurn;

        redPlayer.setPlayersTurn(isRedTurn);
        redPlayer.setHasOfferedDraw(false);

        whitePlayer.setPlayersTurn(!isRedTurn);
        whitePlayer.setHasOfferedDraw(false);
    }

    public boolean isPlayerWhitePlayer(Player player){
        return player == whitePlayer;
    }
}
