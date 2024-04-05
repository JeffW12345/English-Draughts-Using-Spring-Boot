package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServerMessagingOutboundService {
    public static synchronized void sendJsonMessage(String json, String... clientIds) {
        if (json == null || clientIds == null || clientIds.length == 0) {
            log.error("Invalid parameters for sending JSON message");
            return;
        }

        List<Session> sessionsList = new ArrayList<>();
        for (String clientId : clientIds) {
            Session session = ClientIdToSessionMapping.getSessionFromClientId(clientId);
            if (session != null && session.isOpen()) {
                sessionsList.add(session);
            } else {
                log.warn("Session for client ID {} is null or closed", clientId);
            }
        }

        for (Session session : sessionsList) {
            synchronized (session) {
                session.getAsyncRemote().sendText(json);
                log.info("Message sent to client: {}", json);
            }
        }
    }

    public static synchronized void messageBothClientsInAGame(String json, String clientIdOfOneOfPlayers) {
        Game game = ClientIdToGameMapping.getGameForClientId(clientIdOfOneOfPlayers);
        if (game == null) {
            log.error("Game not found for client ID: {}", clientIdOfOneOfPlayers);
            return;
        }

        List<String> clientIdsForGame = ClientIdToGameMapping.getClientIdsForGame(game);
        sendJsonMessage(json, clientIdsForGame.toArray(new String[0]));
    }

    public static synchronized void messageOtherClientInGame(String json, String thisClient) {
        String otherClientId = ServerMessagingUtility.getOtherClientIdForGame(thisClient);
        if (otherClientId != null) {
            sendJsonMessage(json, otherClientId);
        } else {
            log.error("Other client ID not found for client ID: {}", thisClient);
        }
    }
}