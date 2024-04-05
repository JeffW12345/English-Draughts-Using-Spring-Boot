package com.github.jeffw12345.draughts.server.messaging.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessagingUtility {
    public static String convertServerMessageToJSON(ServerMessageToClient serverMessage) {
        String objectAsString = "";
        try {
            objectAsString = new ObjectMapper()
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(serverMessage);
            log.info(String.format("Successfully created JSON from ServerMessageToClient object: %s", objectAsString));

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            log.error("Exiting program due to JSON processing error.");
            System.exit(1); //TODO - Add method to controller for closing both clients gracefully and informing the server
        }
        return objectAsString;
    }

    public static ClientMessageToServer getClientMessageObjectFromJson(String json) {
        try {
            ClientMessageToServer message = new ObjectMapper().readValue(json, ClientMessageToServer.class);
            log.info(String.format("Successfully rendered the following JSON into a ClientMessageToServer object: %s", json));
            return message;
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Error converting JSON to ClientMessageToServer object: {}", jsonProcessingException.getMessage());
            System.exit(1);
            return null; // TODO - Explore how to exit more gracefully.
        }
    }

    public static String getOtherClientIdForGame(String thisClientId){
        Game game = ClientIdToGameMapping.getGameForClientId(thisClientId);
        return game.getOtherClientId(thisClientId);
    }
}
