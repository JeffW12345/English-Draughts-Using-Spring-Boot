package com.github.jeffw12345.draughts.models.mapping;

import com.github.jeffw12345.draughts.models.game.Player;

import java.util.concurrent.ConcurrentHashMap;

public class ClientIdToPlayerMapping {
    private static final ConcurrentHashMap<String, Player> clientIdToPlayer = new ConcurrentHashMap<>();

    public static void assignClientIdToPlayerId(String clientId, Player player){
        clientIdToPlayer.put(clientId, player);
    }
    public static Player getPlayerIdForClientId(String playerId){
        return clientIdToPlayer.get(playerId);
    }
}
