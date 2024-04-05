package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServerMessagingOutboundService {

    public static synchronized void sendJsonMessage(String json, String... clientIds){
        List<Session> sessionsList = new ArrayList<>();
        for(String clientId : clientIds){
            sessionsList.add(ClientIdToSessionMapping.getSessionFromClientId(clientId));
        }
        for (Session session : sessionsList) {
            if (session != null && session.isOpen()) {
                synchronized (session) {
                    session.getAsyncRemote().sendText(json);
                    log.info(String.format("Successfully sent message from server: %s", json));
                }
            }
        }
    }

    public static synchronized void messageBothClientsInAGame(String json, String clientIdOfOneOfPlayers){
        Game game = ClientIdToGameMapping.getGameForClientId(clientIdOfOneOfPlayers);
        List<String> clientIdsForGame = ClientIdToGameMapping.getClientIdsForGame(game);
        sendJsonMessage(json, clientIdsForGame.get(0), clientIdsForGame.get(1));
    }

    public static synchronized void messageOtherClientInGame(String json, String thisClient){
        String otherClientId = ServerMessagingUtility.getOtherClientIdForGame(thisClient);
        sendJsonMessage(json, otherClientId);
    }
}
