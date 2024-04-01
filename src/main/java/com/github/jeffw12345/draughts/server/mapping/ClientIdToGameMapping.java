package com.github.jeffw12345.draughts.server.mapping;

import com.github.jeffw12345.draughts.models.game.Game;

import java.util.concurrent.ConcurrentHashMap;

public class ClientIdToGameMapping {
    private static final ConcurrentHashMap<String, Game> clientIdToGame = new ConcurrentHashMap<>();

    public static void assignClientIdToGame(String clientId, Game game){
        clientIdToGame.put(clientId, game);
    }

    public static Game getGameForClientId(String clientId){
        return clientIdToGame.get(clientId);
    }
}
