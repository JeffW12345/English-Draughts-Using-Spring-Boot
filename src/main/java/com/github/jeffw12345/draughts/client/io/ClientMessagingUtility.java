package com.github.jeffw12345.draughts.client.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMessagingUtility {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertClientMessageToJSON(ClientMessageToServer clientRequestToServer) {
        try {
            return objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(clientRequestToServer);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting message to JSON", e);
        }
    }

    public static ServerMessageToClient getServerMessageObjectFromJson(String json) {
        try {
            return objectMapper.readValue(json, ServerMessageToClient.class);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: {}", e.getMessage());
            throw new RuntimeException("Error parsing JSON message", e);
        }
    }
}
