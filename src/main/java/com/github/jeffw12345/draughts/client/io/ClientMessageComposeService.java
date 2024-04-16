package com.github.jeffw12345.draughts.client.io;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;

import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.DRAW_ACCEPT;
import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.DRAW_OFFER;
import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.RESIGN;
import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.WANT_GAME;

public class ClientMessageComposeService {
    private final Client client;

    public ClientMessageComposeService(Client client) {
        this.client = client;
    }

    public void sendOfferNewGameRequest(String clientId) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(WANT_GAME)
                .build();

        client.getClientOutboundMessagingService().convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendDrawOfferAcceptance(String clientId) {
        ClientMessageToServer acceptDrawMessage = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_ACCEPT)
                .build();

        client.getClientOutboundMessagingService().convertMessageToJSONThenSendToServer(acceptDrawMessage);
    }

    public void sendDrawOfferProposal(String clientId) {
        ClientMessageToServer offerDrawMessage = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_OFFER)
                .build();

        client.getClientOutboundMessagingService().convertMessageToJSONThenSendToServer(offerDrawMessage);
    }

    public void sendResignation(String resigningClientId) {
        ClientMessageToServer resignationRequest = ClientMessageToServer.builder()
                .clientId(resigningClientId)
                .requestType(RESIGN)
                .build();

        client.getClientOutboundMessagingService().convertMessageToJSONThenSendToServer(resignationRequest);
    }
}
