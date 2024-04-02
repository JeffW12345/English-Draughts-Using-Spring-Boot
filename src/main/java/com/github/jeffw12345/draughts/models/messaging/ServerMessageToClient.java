package com.github.jeffw12345.draughts.models.messaging;

import com.github.jeffw12345.draughts.models.game.Game;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerMessageToClient {
    private String clientId;
    private Game game;
    private ServerToClientMessageType serverResponseType;
}
