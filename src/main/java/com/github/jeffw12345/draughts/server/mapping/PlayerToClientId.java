package com.github.jeffw12345.draughts.server.mapping;

import com.github.jeffw12345.draughts.models.game.Player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerToClientId {
    private static final ConcurrentHashMap<Player, String> playerToClientId = new ConcurrentHashMap<>();

    public static void add(Player player, String clientId){
        playerToClientId.put(player, clientId);
    }

    public static String retrieveClientId(Player player){
        return playerToClientId.get(player);
    }
}
