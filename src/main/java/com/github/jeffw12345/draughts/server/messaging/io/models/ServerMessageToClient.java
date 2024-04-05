package com.github.jeffw12345.draughts.server.messaging.io.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jeffw12345.draughts.game.models.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerMessageToClient {
    @JsonProperty("board_state")
    private Board board;

    @JsonProperty("response_type")
    private ServerToClientMessageType serverResponseType;

    @JsonProperty("client_id")
    private String clientId;
}
