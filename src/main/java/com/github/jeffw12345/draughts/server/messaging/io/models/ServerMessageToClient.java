package com.github.jeffw12345.draughts.server.messaging.io.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jeffw12345.draughts.game.models.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerMessageToClient {
    @JsonProperty("board_state")
    private final Board board;

    @JsonProperty("response_type")
    private final ServerToClientMessageType serverResponseType;

    @JsonProperty("client_id")
    private final String clientId;
}
