package com.github.jeffw12345.draughts.models.response;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.Player;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerResponseToClient {
    private Game game;
    private Player player;
    private Move move;
    private ServerResponseType serverResponseType;
    private String informationRequested;
}
