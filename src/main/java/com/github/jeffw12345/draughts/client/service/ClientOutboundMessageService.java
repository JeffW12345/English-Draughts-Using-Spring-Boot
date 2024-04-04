package com.github.jeffw12345.draughts.client.service;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;

import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.*;

@ClientEndpoint
@Slf4j
@Getter
public class ClientOutboundMessageService {
    private Session session;
    private final Client client;

    public ClientOutboundMessageService(Client client) {
        this.client = client;
    }

    public void sendJsonMessageToServer(String jsonMessage) {
        if (session == null){
            session = ClientSessionStorage.retrieveSession();
        }
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(jsonMessage);
            log.info(String.format("Client %s sent message to server: %s", client.getClientId(), jsonMessage));
        }
    }

    public void convertMessageToJSONThenSendToServer(ClientMessageToServer messageAsObject) {
        String messageAsJSON = ClientMessagingUtility.convertClientMessageToJSON(messageAsObject);
        sendJsonMessageToServer(messageAsJSON);
    }

    public void sendMoveToServer(Move move) {
        if (client != null && client.getClientController() != null) {
            Colour clientPlayerColour = client.getClientController().isAmIRed() ? Colour.RED : Colour.WHITE;
            ClientMessageToServer moveRequest = ClientMessageToServer.builder()
                    .clientId(client.getClientId())
                    .move(move)
                    .colourOfClientPlayer(clientPlayerColour)
                    .requestType(MOVE_REQUEST)
                    .build();

            convertMessageToJSONThenSendToServer(moveRequest);
        } else {
            log.error("Client or ClientController is null");
        }
    }

    public void sendOfferNewGameRequest(String clientId) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(WANT_GAME)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendDrawOfferAcceptance(String clientId) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_ACCEPT)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendDrawOfferProposal(String clientId) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_OFFER)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendResignation(String resigningClientId) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(resigningClientId)
                .requestType(RESIGN)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void establishSession() {
        try {
            ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, new URI("ws://localhost:8080/webSocket"));

            ClientMessageToServer clientMessage = ClientMessageToServer.builder()
                    .session(session)
                    .clientId(client.getClientId())
                    .requestType(ESTABLISH_SESSION)
                    .build();

            convertMessageToJSONThenSendToServer(clientMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void tellServerClientExitedThenCloseSession(String windowClosedClientId, String message) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(windowClosedClientId)
                .requestType(EXIT)
                .information(message)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
        closeSession();
    }

    public void closeSession() {
        try {
            log.warn("About to close communication session with server. You will receive a confirmation message.");
            session.close();
        } catch (IOException e) {
            log.error(String.format("Problem closing session: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}
