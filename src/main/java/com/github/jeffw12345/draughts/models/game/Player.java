package com.github.jeffw12345.draughts.models.game;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Player {
    private String playerId = String.valueOf(UUID.randomUUID());
    private boolean isPlayersTurn;
    private boolean hasResigned = false;
    private boolean hasOfferedDraw = false;
    private boolean hasAcceptedDraw = false;
    private boolean amIRedPlayer; //TODO - Make sure that this is notified.

}
