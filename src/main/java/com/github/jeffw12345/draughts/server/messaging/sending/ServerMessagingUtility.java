package com.github.jeffw12345.draughts.server.messaging.sending;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
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
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            log.error("Exiting program due to JSON processing error.");
            System.exit(1); // TODO - Add method to controller for closing both clients gracefully and informing the server
        }
        return objectAsString;
    }

    public static ClientMessageToServer getClientMessageObject(String json) {
        ClientMessageToServer message = null;
        try{
            message = new ObjectMapper().readValue(json, ClientMessageToServer.class);
        } catch(JsonProcessingException jsonProcessingException){
            log.error(jsonProcessingException.getMessage()); //TODO - Code to exit gracefully
        }
        return message;
    }

}
