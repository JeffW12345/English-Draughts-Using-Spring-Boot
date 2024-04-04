package com.github.jeffw12345.draughts.client.io;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@ClientEndpoint
@Slf4j
@Getter
public class ClientInboundMessageService {
    private Session session;
    private final Client client;
    private String sessionId;

    public ClientInboundMessageService(Client client) {
        this.client = client;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.sessionId = session.getId();

        ClientSessionStorage.storeSession(session);

        log.info("Session with server set up");
    }

    @OnClose
    public static synchronized void onClose(Session session) {
        log.info("Session disconnected");
    }

    @OnMessage
    public void onMessage(String jsonMessage) {
        log.info(String.format("Incoming message to client id %s from server: %s", client.getClientId(), jsonMessage));
        ServerMessageToClient messageFromServerAsObject = ClientMessagingUtility.getServerMessageObjectFromJson(jsonMessage);
        client.getClientController().processMessageFromServer(messageFromServerAsObject);
    }
}
