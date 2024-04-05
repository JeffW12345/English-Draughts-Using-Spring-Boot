package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
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

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.INFORM_CLIENT_OF_ID;

@Getter
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket")
public class ServerMessagingInboundService {

    private final static Set<Session> SESSIONS = new CopyOnWriteArraySet<>();
    @OnOpen
    public static synchronized void onOpen(Session session) {
        SESSIONS.add(session);
        String clientId = String.valueOf(UUID.randomUUID());
        ClientIdToSessionMapping.add(clientId, session);
        sendClientIdToClient(clientId, session);
        log.info(String.format("New server session. Number of sessions: %s", SESSIONS.size()));
    }

    private static void sendClientIdToClient(String clientId, Session session) {
        //TODO - Code to create object for giving client its id. Also do a CountDownLatch.
        // Create new thread, wait for clientId to not be null, then latch.countDown()
        try {
            ServerMessageToClient informClientOfIdAsObject = ServerMessageToClient.builder()
                    .serverResponseType(INFORM_CLIENT_OF_ID)
                    .clientId(clientId)
                    .build();

            String informClientOfIdAsJson =ServerMessagingUtility.convertServerMessageToJSON(informClientOfIdAsObject);
            session.getBasicRemote().sendText(informClientOfIdAsJson);
        } catch (IOException e) {
            log.error("Error sending message to client: {}", e.getMessage());
        }
    }

    @OnMessage
    public static synchronized void onMessage(String message) {
        log.error("TEST"); //TODO - Delete
        ClientMessageToServer messageFromClient = ServerMessagingUtility.getClientMessageObjectFromJson(message);

        log.info(String.format("New message received from client id %s by server: %s",
                messageFromClient.getClientId(),
                message));

        updateClientIdToSessionDictionary(messageFromClient);

        ServerMessageController.processClientRequest(messageFromClient);
    }

    private static void updateClientIdToSessionDictionary(ClientMessageToServer message) {
        String clientId = message.getClientId();
        Session session = ClientIdToSessionMapping.getSessionFromClientId(clientId);
        ClientIdToSessionMapping.add(clientId, session);
    }

    @OnClose
    public static synchronized void onClose(Session session) {
        String clientIdForSession = ClientIdToSessionMapping.getClientIdForSession(session);
        ClientIdToSessionMapping.remove(clientIdForSession);

        log.info(String.format("Session disconnected. Number of sessions: %s", SESSIONS.size()));
    }
    @OnError
    public static synchronized void onError(Session session, Throwable throwable) {
        log.error(String.format("Session exception thrown. Details: %s", throwable.getMessage()));
    }
}