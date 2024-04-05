package com.github.jeffw12345.draughts.client.io.models;

import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.move.Move;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ClientMessageToServer {
    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("requestType")
    private ClientToServerMessageType requestType;

    @JsonProperty("move")
    private Move move;

    @JsonProperty("colourOfClientPlayer")
    private Colour colourOfClientPlayer;
}
