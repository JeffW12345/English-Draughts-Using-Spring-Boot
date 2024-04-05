package com.github.jeffw12345.draughts.models.messaging;

import com.github.jeffw12345.draughts.models.game.Board;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerMessageToClient {
    private Board board;
    private ServerToClientMessageType serverResponseType;

    public String clientId;
}
