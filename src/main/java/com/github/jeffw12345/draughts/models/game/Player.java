package com.github.jeffw12345.draughts.models.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    //private final String PLAYER_ID = String.valueOf(UUID.randomUUID());

    private String playerId;
    private boolean isPlayersTurn;
}
