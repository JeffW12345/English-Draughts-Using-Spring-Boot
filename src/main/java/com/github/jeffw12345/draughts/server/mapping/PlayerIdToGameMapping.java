package com.github.jeffw12345.draughts.server.mapping;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerIdToGameMapping {
    private static final ConcurrentHashMap<String, Game> playerIdToGame = new ConcurrentHashMap<>();

    public synchronized static void add(Player player, Game game){
        playerIdToGame.put(player.getPlayerId(), game);
    }

    private synchronized static List<Game> getListOfGamesAwaitingPlayers() {
        return playerIdToGame.values().stream().filter(Game::awaitingNewGame).toList();
    }

    public static Game getGameForPlayer(Player player){
        return playerIdToGame.get(player.getPlayerId());
    }

    public static boolean doesGameExistForPlayer(Player player){
        return playerIdToGame.containsKey(player.getPlayerId());
    }
}
