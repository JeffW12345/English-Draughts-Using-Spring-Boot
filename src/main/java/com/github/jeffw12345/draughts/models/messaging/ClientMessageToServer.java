package com.github.jeffw12345.draughts.models.messaging;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.move.Move;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientMessageToServer {
    private Client client;
    private String clientId;
    private ClientToServerMessageType requestType;
    private Game game;
    private String sessionId;
    private Move move;
    private Colour colourOfClientPlayer;
}