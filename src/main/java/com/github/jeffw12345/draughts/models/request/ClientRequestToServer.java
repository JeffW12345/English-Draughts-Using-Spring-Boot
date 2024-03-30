package com.github.jeffw12345.draughts.models.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.Player;
import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientRequestToServer {
    private String clientId;
    private Client client;
    private ClientToServerRequestType requestType;
    private Game game;
    private Player player;
    private Move move;

    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServerResponseToClient makeServerRequestAndGetResponse() {
        try {
            MessageToServerService messageDispatch = new MessageToServerService(client);
            messageDispatch.sendMessage(this.toString());
            String response = messageDispatch.responseMessage();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, ServerResponseToClient.class);
        } catch (JsonProcessingException e) {
            //TODO - Proper logging. Copy logs to file. Need to include client id.
            e.printStackTrace();
            return null;
        }
    }
}