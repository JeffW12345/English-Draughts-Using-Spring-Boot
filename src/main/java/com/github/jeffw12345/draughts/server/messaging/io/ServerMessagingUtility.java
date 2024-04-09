package com.github.jeffw12345.draughts.server.messaging.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessagingUtility {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Object lock = new Object();

    public static String convertServerMessageToJSON(ServerMessageToClient serverMessage) {
        synchronized (lock){
            try {
                String objectAsString = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(serverMessage);
                log.info("Successfully created JSON from ServerMessageToClient object: {}", objectAsString);
                return objectAsString;
            } catch (JsonProcessingException e) {
                log.error("Error converting ServerMessageToClient object to JSON: {}", e.getMessage());
                throw new RuntimeException("Failed to convert ServerMessageToClient object to JSON", e);
            }
        }
    }

    public static ClientMessageToServer getClientMessageObjectFromJson(String json) {
        synchronized (lock){
            try {
                ClientMessageToServer message = objectMapper.readValue(json, ClientMessageToServer.class);
                log.info("Successfully rendered JSON into a ClientMessageToServer object: {}", json);
                return message;
            } catch (JsonProcessingException e) {
                log.error("Error converting JSON to ClientMessageToServer object: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid JSON format for ClientMessageToServer", e);
            }
        }
    }

    public static String getOtherClientIdForGame(String thisClientId) {
        synchronized (lock){
            Game game = ClientIdToGameMapping.getGameForClientId(thisClientId);
            if (game == null) {
                log.error("Game not found for client ID: {}", thisClientId);
                throw new IllegalArgumentException("Game not found for client ID: " + thisClientId);
            }
            String otherClientId = game.getOtherClientId(thisClientId);
            if (otherClientId == null) {
                log.error("Other client ID not found for game with client ID: {}", thisClientId);
                throw new IllegalArgumentException("Other client ID not found for game with client ID: " + thisClientId);
            }
            return otherClientId;
        }
    }
}