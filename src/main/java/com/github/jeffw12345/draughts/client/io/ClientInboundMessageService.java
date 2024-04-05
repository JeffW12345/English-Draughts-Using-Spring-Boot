package com.github.jeffw12345.draughts.client.io;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@ClientEndpoint
@Slf4j
@Getter
@Builder
public class ClientInboundMessageService {
    private final Client client;
    public ClientInboundMessageService(Client client) {
        this.client = client;
    }
    @OnClose
    public void onClose(Session session) {
        log.info("Session disconnected for client id {}", client.getClientId());
    }

    @OnMessage
    public void onMessage(String jsonMessage) {
        log.info("Incoming message to client id {} from server: {}", client.getClientId(), jsonMessage);
        try {
            ServerMessageToClient messageFromServerAsObject = ClientMessagingUtility
                    .getServerMessageObjectFromJson(jsonMessage);
            client.getClientController().processMessageFromServer(messageFromServerAsObject);
        } catch (Exception e) {
            log.error("Error processing message from server: {}", e.getMessage());
        }
    }
}
