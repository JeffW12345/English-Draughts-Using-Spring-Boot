package com.github.jeffw12345.draughts.models.messaging;

import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;

import jakarta.websocket.Session;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientMessageToServer {
    private Session session;
    private String clientId;
    private ClientToServerMessageType requestType;
    private Move move;
    private Colour colourOfClientPlayer;
}