package com.github.jeffw12345.draughts.models.game;

import java.util.UUID;

public class Game {
    private final String GAME_ID = String.valueOf(UUID.randomUUID());
    private Board currentBoard;
    private Player redPlayer;
    private Player whitePlayer;
    private boolean isRedTurn;
    private GameStatus gameStatus;



}
