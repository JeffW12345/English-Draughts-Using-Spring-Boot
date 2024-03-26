package com.github.jeffw12345.draughts.models.mapping;

import com.github.jeffw12345.draughts.models.game.Game;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerIdToGameMapping {
    private static final ConcurrentHashMap<String, Game> playerIdToGame = new ConcurrentHashMap<>();

    public synchronized static void assignPlayerIdToGame(String playerId){
        List<Game> listOfGamesAwaitingPlayers = getListOfGamesAwaitingPlayers();
        if(listOfGamesAwaitingPlayers.size() == 0){
            Game game = new Game();
            game.addPlayer(PlayerIdToPlayerMapping.getPlayerFromId(playerId));
            playerIdToGame.put(playerId, game);
        }else{
            Game game = listOfGamesAwaitingPlayers.get(0);
            game.addPlayer(PlayerIdToPlayerMapping.getPlayerFromId(playerId));
            playerIdToGame.put(playerId, game);
            game.changeStatusToInProgress();
            game.notifyPlayersGameCreated();
            //TODO - Create player id to client id mapping.
        }
    }

    private synchronized static List<Game> getListOfGamesAwaitingPlayers() {
        return playerIdToGame.values().stream().filter(Game::awaitingNewGame).toList();
    }

    public static Game getGameForPlayerId(String playerId){
        return playerIdToGame.get(playerId);
    }

    public static boolean doesGameExistForPlayerId(String playerId){
        return playerIdToGame.containsKey(playerId);
    }
}
