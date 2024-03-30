package com.github.jeffw12345.draughts.server.mapping;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerIdToGameMapping {
    private static final ConcurrentHashMap<Player, Game> playerToGame = new ConcurrentHashMap<>();

    //TODO - Add functionality to create a new Game once 2+ players waiting.
    public synchronized static void assignPlayerToGame(Player player, Game game){
        playerToGame.put(player, game);
    }

    private synchronized static List<Game> getListOfGamesAwaitingPlayers() {
        return playerToGame.values().stream().filter(Game::awaitingNewGame).toList();
    }

    public static Game getGameForPlayer(Player player){
        return playerToGame.get(player);
    }

    public static boolean doesGameExistForPlayer(Player player){
        return playerToGame.containsKey(player);
    }
}
