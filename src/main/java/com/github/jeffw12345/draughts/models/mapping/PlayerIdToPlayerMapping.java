package com.github.jeffw12345.draughts.models.mapping;

import com.github.jeffw12345.draughts.models.game.Player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerIdToPlayerMapping {
    private static final ConcurrentHashMap<String, Player> playerIdToPlayer = new ConcurrentHashMap<>();

    public static Player getPlayerFromId(String playerId){
        return playerIdToPlayer.get(playerId);
    }

    public static void storePlayer(String playerId, Player player){
        playerIdToPlayer.put(playerId, player);
    }
}
