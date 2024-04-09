package com.github.jeffw12345.draughts.game.models;

import com.github.jeffw12345.draughts.game.models.move.Move;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;


@Setter
@Getter
public class Game {
    private String gameId = String.valueOf(UUID.randomUUID());
    private Board currentBoard = new Board();
    private boolean isRedTurn = true, isDrawOfferPending, isNewTurn;
    private static final ConcurrentLinkedDeque<Move>  redPlayerMoves = new ConcurrentLinkedDeque<>();
    private static final ConcurrentLinkedDeque<Move>  whitePlayerMoves = new ConcurrentLinkedDeque<>();

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

    public void changeTurns(){
        isRedTurn = !isRedTurn;
        isDrawOfferPending = false;
        isNewTurn = true;
    }
    public List<String> getClientIds(){
        return ClientIdToGameMapping.getClientIdsForGame(this);
    }

    public String getOtherClientId(String aClientId){
        return getClientIds().stream()
                .filter(clientId -> !clientId.equals(aClientId))
                .findFirst()
                .orElse(null);
    }
}
