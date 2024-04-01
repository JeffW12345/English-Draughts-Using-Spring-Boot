package com.github.jeffw12345.draughts.models.messaging;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.Player;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerMessageToClient {
    private String clientId;
    private Game game;
    private Move move; //TODO - Give each game a list of moves then remove this.
    private ServerResponseType serverResponseType;
    private String sessionId;
    private String information; // TODO - Necessary?
}
