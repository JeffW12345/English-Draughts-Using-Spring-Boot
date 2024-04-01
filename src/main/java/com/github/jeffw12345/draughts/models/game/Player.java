package com.github.jeffw12345.draughts.models.game;

import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.mapping.PlayerToClientId;
import com.github.jeffw12345.draughts.server.message.ServerMessagingService;
import com.github.jeffw12345.draughts.server.message.ServerMessagingUtility;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static com.github.jeffw12345.draughts.models.messaging.ServerResponseType.ASSIGN_RED_COLOUR;
import static com.github.jeffw12345.draughts.models.messaging.ServerResponseType.ASSIGN_WHITE_COLOUR;

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

    public void newGameClientNotifications(Game game) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(amIRedPlayer ? ASSIGN_RED_COLOUR : ASSIGN_WHITE_COLOUR)
                .clientId(clientId)
                .game(game)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);
        ServerMessagingService.sendMessage(messageAsJson, PlayerToClientId.retrieveClientId(this));
    }
}
