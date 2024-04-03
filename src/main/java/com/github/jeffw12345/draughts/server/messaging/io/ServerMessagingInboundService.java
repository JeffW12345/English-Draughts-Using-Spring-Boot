package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import com.github.jeffw12345.draughts.server.mapping.SessionIdToSessionMapping;
import com.github.jeffw12345.draughts.server.messaging.processing.ServerMessageController;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@ClientEndpoint
@Getter
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket")
public class ServerMessagingInboundService {
    @OnOpen
    public static synchronized void onOpen(Session session) {
        SessionIdToSessionMapping.add(session);
        log.info(String.format("New server session. Number of sessions: %s", SessionIdToSessionMapping.getSize()));
    }

    @OnMessage
    public static synchronized void onMessage(String message) {
        log.info(String.format("New message received by server: %s", message));

        ClientMessageToServer messageFromClient = ServerMessagingUtility.getClientMessageObjectFromJson(message);

        updateClientIdToSessionDictionary(messageFromClient);

        ServerMessageController.processClientRequest(messageFromClient);
    }

    private static void updateClientIdToSessionDictionary(ClientMessageToServer message) {
        String clientId = message.getClientId();
        Session session = SessionIdToSessionMapping.getSessionForSessionId(message.getSession().getId());
        ClientIdToSessionMapping.add(clientId, session);
    }

    @OnClose
    public static synchronized void onClose(Session session) {
        SessionIdToSessionMapping.remove(session.getId());

        String clientIdForSession = ClientIdToSessionMapping.getClientIdForSession(session);
        ClientIdToSessionMapping.remove(clientIdForSession);

        log.info(String.format("Session disconnected. Number of sessions: %s", SessionIdToSessionMapping.getSize()));
    }
    @OnError
    public static synchronized void onError(Session session, Throwable throwable) {
        log.error(throwable.getMessage());
    }
}