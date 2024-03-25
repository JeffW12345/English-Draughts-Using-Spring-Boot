package com.github.jeffw12345.draughts.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.controller.ClientController;
import com.github.jeffw12345.draughts.client.controller.MessageToServerService;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;
import com.github.jeffw12345.draughts.models.request.ClientRequestToServer;
import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;

import static com.github.jeffw12345.draughts.models.request.ClientToServerRequestType.WANT_PLAYER_ID;

public class Client {
    private Player player = new Player();
    private Game game = new Game();

    public Client() {
        initialSetupActions();
    }

    private void initialSetupActions() {
        String playerId = requestPlayerId();
        player.setPlayerId(playerId);
        ClientController clientController = new ClientController();
    }

    private String requestPlayerId() {
        try {
            ClientRequestToServer requestForPlayerId = ClientRequestToServer.builder()
                    .requestType(WANT_PLAYER_ID)
                    .build();

            MessageToServerService messageDispatch = new MessageToServerService();
            messageDispatch.sendMessage(requestForPlayerId.toString());
            String response = messageDispatch.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            ServerResponseToClient serverResponseToClient = objectMapper.readValue(response, ServerResponseToClient.class);
            return serverResponseToClient.getInformationRequested();
        } catch (JsonProcessingException e) {
            //TODO - Proper logging
            e.printStackTrace();
            return null;
        }
    }

}

