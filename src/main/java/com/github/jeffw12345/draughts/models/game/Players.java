package com.github.jeffw12345.draughts.models.game;

import java.util.concurrent.ConcurrentHashMap;

public class Players {
    private final ConcurrentHashMap<String, Player> playerIdToPlayer = new ConcurrentHashMap<>();

    public Player getPlayerFromId(String playerId){
        return playerIdToPlayer.get(playerId);
    }

    public void storePlayer(String playerId, Player player){
        playerIdToPlayer.put(playerId, player);
    }
}
