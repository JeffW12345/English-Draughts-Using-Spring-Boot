package com.github.jeffw12345.draughts.models.game;

import java.util.concurrent.ConcurrentHashMap;

public class GameReservations {
    private final ConcurrentHashMap<String, Game> playerIdToGame = new ConcurrentHashMap<>();

    public void assignPlayerIdToGame(String playerId, Game game){
        playerIdToGame.put(playerId, game);
    }

    public Game getGameForPlayerId(String playerId){
        return playerIdToGame.get(playerId);
    }

    public boolean doesGameExistForPlayerId(String playerId){
        return playerIdToGame.containsKey(playerId);
    }
}
