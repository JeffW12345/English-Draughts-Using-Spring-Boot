package com.github.jeffw12345.draughts.models.game;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Game {
    private String gameId;
    private Board currentBoard;
    private Player redPlayer;
    private Player whitePlayer;
    private boolean isRedTurn;
    private GameStatus gameStatus = GameStatus.AWAITING_NEW_GAME;

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

    public void changeStatusToInProgress(){
        gameStatus = GameStatus.IN_PROGRESS;
    }

    public void notifyPlayersGameCreated() {
        redPlayer.notifyClientGameInProgress(this, Colour.RED);
        whitePlayer.notifyClientGameInProgress(this, Colour.WHITE);
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
