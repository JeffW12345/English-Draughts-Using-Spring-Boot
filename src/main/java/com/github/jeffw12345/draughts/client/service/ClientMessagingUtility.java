package com.github.jeffw12345.draughts.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import lombok.extern.slf4j.Slf4j; //TODO - Add to other classes

@Slf4j
public class ClientMessagingUtility {

    public static String convertClientMessageToJSON(ClientMessageToServer clientRequestToServer) {
        String objectAsString = "";
        try {
            objectAsString = new ObjectMapper()
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(clientRequestToServer);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            log.error("Exiting program due to JSON processing error.");
            System.exit(1);
        }
        return objectAsString;
    }

    public static ServerMessageToClient getServerMessageObjectFromJson(String json) {
        ServerMessageToClient serverMessageToClient = null;
        try{
            serverMessageToClient = new ObjectMapper().readValue(json, ServerMessageToClient.class);
        } catch(JsonProcessingException jsonProcessingException){
            log.error(jsonProcessingException.getMessage());
            log.error("Exiting program due to JSON processing error.");
            System.exit(1);
        }
        return serverMessageToClient;
    }
}
