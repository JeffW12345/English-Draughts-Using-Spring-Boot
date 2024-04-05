package com.github.jeffw12345.draughts.game.models;

import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.messaging.io.ServerMessagingOutboundService;
import com.github.jeffw12345.draughts.server.messaging.io.ServerMessagingUtility;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static com.github.jeffw12345.draughts.server.messaging.io.models.ServerToClientMessageType.ASSIGN_RED_COLOUR;
import static com.github.jeffw12345.draughts.server.messaging.io.models.ServerToClientMessageType.ASSIGN_WHITE_COLOUR;

@Getter
@Setter
public class Player {
    private String clientId;
    private String playerId = String.valueOf(UUID.randomUUID());
    private boolean isPlayersTurn;
    private boolean hasResigned = false;
    private boolean hasOfferedDraw = false;
    private boolean hasAcceptedDraw = false;
    private boolean amIRedPlayer;

    public Player(String clientId, Colour red){
        this.clientId = clientId;
        this.amIRedPlayer = red == Colour.RED;
    }

    public void newGameClientNotifications() {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(amIRedPlayer ? ASSIGN_RED_COLOUR : ASSIGN_WHITE_COLOUR)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);
        ServerMessagingOutboundService.sendJsonMessage(messageAsJson, clientId);
    }
}