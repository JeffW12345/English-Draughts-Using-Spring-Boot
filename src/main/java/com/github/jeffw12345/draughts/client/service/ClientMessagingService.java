package com.github.jeffw12345.draughts.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.ESTABLISH_SESSION;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.MOVE_REQUEST;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.WANT_GAME;

@ClientEndpoint
@Slf4j
@Getter
public class ClientMessagingService {
    private Session session;
    private final Client client;
    private String sessionId;

    public ClientMessagingService(Client client) {
        this.client = client;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.sessionId = session.getId();
    }

    @OnMessage
    public void onMessage(String message) {
        ServerMessageToClient messageFromServer;
        try{
            messageFromServer = new ObjectMapper().readValue(message, ServerMessageToClient.class);
            client.getClientController().processMessageFromServer(messageFromServer);
        } catch(JsonProcessingException jsonProcessingException){
            log.error(jsonProcessingException.getMessage());}
    }

    public void sendMessageToServer(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public void sendMessageToServer(ClientMessageToServer messageAsObject) {
        String messageAsJSON = ClientMessagingUtility.convertClientMessageToJSON(messageAsObject);
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(messageAsJSON);
        }
    }

    public void sendMoveToServer(Move move) {
        Colour clientPlayerColour = client.getClientController().isAmIRed() ? Colour.RED : Colour.WHITE;
        ClientMessageToServer moveRequest = ClientMessageToServer.builder()
                .clientId(client.getClientId())
                .move(move)
                .colourOfClientPlayer(clientPlayerColour)
                .requestType(MOVE_REQUEST)
                .build();

        sendMessageToServer(moveRequest);
    }

    public void sendOfferNewGameRequest(Client client){
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .client(client)
                .requestType(WANT_GAME)
                .build();

        sendMessageToServer(requestForGame);
    }

    public void sendDrawOfferAcceptance(Client client){
        //TODO
    }

    public void sendDrawOfferProposal(Client client){
        //TODO
    }

    public void sendResignation(Client client){
        //TODO
    }

    public void establishSession() {
        try {
            ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, new URI("ws://localhost:8080/webSocket"));

            ClientMessageToServer clientMessage = ClientMessageToServer.builder()
                    .clientId(client.getClientId())
                    .sessionId(sessionId)
                    .requestType(ESTABLISH_SESSION)
                    .build();

            String connectionMessage = ClientMessagingUtility.convertClientMessageToJSON(clientMessage);

            sendMessageToServer(connectionMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
