package com.github.jeffw12345.draughts.server.messaging.io.models;

import com.github.jeffw12345.draughts.game.models.Board;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerMessageToClient {
    private Board board;
    private ServerToClientMessageType serverResponseType;

    public String clientId;
}
