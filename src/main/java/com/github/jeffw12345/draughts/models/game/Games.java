package com.github.jeffw12345.draughts.models.game;

import java.util.concurrent.ConcurrentHashMap;

public class Games {
    private final ConcurrentHashMap<String, Game> gameIdToGame = new ConcurrentHashMap<>();

    public Game getGameFromId(String gameId){
        return gameIdToGame.get(gameId);
    }
    public void storeGame(String gameId, Game game){
        gameIdToGame.put(gameId, game);
    }
}
