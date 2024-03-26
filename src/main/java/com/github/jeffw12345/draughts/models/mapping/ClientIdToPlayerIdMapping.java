package com.github.jeffw12345.draughts.models.mapping;

import com.github.jeffw12345.draughts.models.game.Game;

import java.util.concurrent.ConcurrentHashMap;

public class ClientIdToPlayerIdMapping {
    private static final ConcurrentHashMap<String, String> clientIdToPlayerId = new ConcurrentHashMap<>();

    public static void assignClientIdToPlayerId(String clientId, String playerId){
        clientIdToPlayerId.put(clientId, playerId);
    }
    public static String getPlayerIdForClientId(String playerId){
        return clientIdToPlayerId.get(playerId);
    }
}
