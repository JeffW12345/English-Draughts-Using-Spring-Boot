package com.github.jeffw12345.draughts.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
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

import static com.github.jeffw12345.draughts.models.messaging.message.ClientToServerRequestType.ESTABLISH_CONNECTION;

@ClientEndpoint
@Slf4j
@Getter
public class ClientMessagingService {
    private Session session;
    private Client client;

    private String sessionId;

    public ClientMessagingService(Client client) {
        this.client = client;
    }

    // Run when a session with the server is created.
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.sessionId = session.getId();
    }

    // Processes incoming messages.
    @OnMessage
    public void onMessage(String message) {
        ServerMessageToClient serverResponseToClient;
        try{
            serverResponseToClient = new ObjectMapper().readValue(message, ServerMessageToClient.class);
            client.getClientController().processMessageFromServer(serverResponseToClient);
        } catch(JsonProcessingException jsonProcessingException){
            log.error(jsonProcessingException.getMessage());}
    }

    public void sendMessageToServer(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public void establishConnection() {
        try {
            ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, new URI("ws://localhost:8080/webSocket"));

            ClientMessageToServer clientMessage = ClientMessageToServer.builder()
                    .clientId(this.client.getCLIENT_ID())
                    .sessionId(this.sessionId)
                    .requestType(ESTABLISH_CONNECTION)
                    .build();

            String connectionMessage = ClientMessagingUtility.convertClientMessageToJSON(clientMessage);

            sendMessageToServer(connectionMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
