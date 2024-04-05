package com.github.jeffw12345.draughts.server.mapping;

import com.github.jeffw12345.draughts.game.models.Game;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClientIdToGameMapping {
    private static final ConcurrentHashMap<String, Game> clientIdToGame = new ConcurrentHashMap<>();

    public static void assignClientIdToGame(String clientId, Game game){
        clientIdToGame.put(clientId, game);
    }
    public static Game getGameForClientId(String clientId){
        return clientIdToGame.get(clientId);
    }

    public static List<String> getClientIdsForGame(Game game) {
        return clientIdToGame.entrySet().stream()
                .filter(entry -> entry.getValue().equals(game))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
