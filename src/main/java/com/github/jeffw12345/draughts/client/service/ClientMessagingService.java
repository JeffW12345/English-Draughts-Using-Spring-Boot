package com.github.jeffw12345.draughts.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.request.ClientRequestToServer;
import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import static com.github.jeffw12345.draughts.models.request.ClientToServerRequestType.ESTABLISH_CONNECTION;

@ClientEndpoint
@Slf4j
@Getter
public class ClientMessagingService {
    private Session session;
    private Client client;

    public ClientMessagingService(Client client) {
        this.client = client;
    }

    // Run when a session with the server is created.
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    // Processes incoming messages.
    @OnMessage
    public void onMessage(String message) {
        ServerResponseToClient serverResponseToClient;
        try{
            serverResponseToClient = new ObjectMapper().readValue(message, ServerResponseToClient.class);
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
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8080/webSocket"));
            ClientRequestToServer clientRequestToServer = ClientRequestToServer.builder()
                    .clientId(this.client.getCLIENT_ID())
                    .requestType(ESTABLISH_CONNECTION)
                    .build();
            String connectionMessage = new ObjectMapper()
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(clientRequestToServer);

            sendMessageToServer(connectionMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
