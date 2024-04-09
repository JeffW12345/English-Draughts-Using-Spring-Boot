package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import com.github.jeffw12345.draughts.server.messaging.processing.ServerMessageController;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.github.jeffw12345.draughts.server.messaging.io.models.ServerToClientMessageType.INFORM_CLIENT_OF_ID;

@Getter
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket")
public class ServerMessagingInboundService {

    private final static Set<Session> SESSIONS = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        SESSIONS.add(session);
        log.info("New server session established. Number of sessions: {}", SESSIONS.size());

        String clientId = UUID.randomUUID().toString();
        ClientIdToSessionMapping.addMapping(clientId, session);
        sendClientIdToClient(clientId);
    }

    private void sendClientIdToClient(String clientId) {
        ServerMessageToClient informClientOfIdAsObject = ServerMessageToClient.builder()
                .serverResponseType(INFORM_CLIENT_OF_ID)
                .clientId(clientId)
                .build();

        String informClientOfIdAsJson = ServerMessagingUtility.convertServerMessageToJSON(informClientOfIdAsObject);

        log.info("Informing client {} of its ID", clientId);

        ServerMessagingOutboundService.sendJsonMessage(informClientOfIdAsJson, clientId);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("New message received from client: {}", message);
        ClientMessageToServer messageFromClient = ServerMessagingUtility.getClientMessageObjectFromJson(message);

        updateClientIdToSessionDictionary(messageFromClient);

        ServerMessageController.processClientRequest(messageFromClient);
    }

    private void updateClientIdToSessionDictionary(ClientMessageToServer message) {
        String clientId = message.getClientId();
        if (clientId != null){
            Session session = ClientIdToSessionMapping.getSessionFromClientId(clientId);
            ClientIdToSessionMapping.addMapping(clientId, session);
        }
    }

    @OnClose
    public void onClose(Session session) {
        String clientIdForSession = ClientIdToSessionMapping.getClientIdForSession(session);
        ClientIdToSessionMapping.removeEntryFromMap(clientIdForSession);

        log.info("Session disconnected. Number of sessions: {}", SESSIONS.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Session exception thrown. Details: {}", throwable.getMessage());
    }
}