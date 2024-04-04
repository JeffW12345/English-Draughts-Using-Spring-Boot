package com.github.jeffw12345.draughts.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import com.github.jeffw12345.draughts.server.mapping.SessionIdToSessionMapping;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;

import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.DRAW_ACCEPT;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.DRAW_OFFER;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.ESTABLISH_SESSION;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.EXIT;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.MOVE_REQUEST;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.RESIGN;
import static com.github.jeffw12345.draughts.models.messaging.ClientToServerMessageType.WANT_GAME;

@ClientEndpoint
@Slf4j
@Getter
public class ClientMessageDispatchService {
    private Session session;
    private final Client client;
    private String sessionId;

    public ClientMessageDispatchService(Client client) {
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

    @OnClose
    public static synchronized void onClose(Session session) {
        log.info("Session disconnected");
    }

    public void sendJsonMessageToServer(String jsonMessage) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(jsonMessage);
        }
    }

    public void convertMessageToJSONThenSendToServer(ClientMessageToServer messageAsObject) {
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

        convertMessageToJSONThenSendToServer(moveRequest);
    }

    public void sendOfferNewGameRequest(String clientId){
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(WANT_GAME)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendDrawOfferAcceptance(String clientId){
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_ACCEPT)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendDrawOfferProposal(String clientId){
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_OFFER)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendResignation(String resigningClientId){
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

    public void closeSession(){
        try {
            log.warn("About to close communication session with server. You will receive a confirmation message.");
            session.close();
        } catch (IOException e) {
            log.error(String.format("Problem closing session: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}
