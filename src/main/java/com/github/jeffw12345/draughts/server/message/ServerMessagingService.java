package com.github.jeffw12345.draughts.server.message;

import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ClientEndpoint
@Getter
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket")
public class ServerMessagingService {
    private static final ConcurrentHashMap<String, Session> sessionIdToSession = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Session> clientIdToSession = new ConcurrentHashMap<>();
    @OnOpen
    public static synchronized void onOpen(Session session) {
        sessionIdToSession.put(session.getId(), session);
        log.info(String.format("New server session. Number of sessions: %s", sessionIdToSession.size()));
    }

    @OnMessage
    public static synchronized void onMessage(String message) {
        log.info(String.format("New message received by server: %s", message));

        ClientMessageToServer messageFromClient = ServerMessagingUtility.getClientMessageObject(message);

        updateClientIdToSessionDictionary(messageFromClient);

        MessageController.processMessageFromClient(messageFromClient);
    }

    private static void updateClientIdToSessionDictionary(ClientMessageToServer message) {
        String clientId = message.getClientId();
        Session session = sessionIdToSession.get(message.getSessionId());
        clientIdToSession.put(clientId, session);
    }

    @OnClose
    public static synchronized void onClose(Session session) {
        sessionIdToSession.remove(session.getId());
        String clientIdForSession = clientIdToSession.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);

        assert clientIdForSession != null;
        clientIdToSession.remove(clientIdForSession);
        log.info(String.format("Session disconnected. Number of sessions: %s", sessionIdToSession.size()));
    }
    @OnError
    public static synchronized void onError(Session session, Throwable throwable) {
        log.error(throwable.getMessage());
    }

    public static synchronized void sendMessage(String json, String... clientIds){
        List<Session> sessionsList = new ArrayList<>();
        for(String clientId : clientIds){
            sessionsList.add(clientIdToSession.get(clientId));
        }
        for (Session session : sessionsList) {
            if (session != null && session.isOpen()) {
                synchronized (session) {
                    try {
                        session.getBasicRemote().sendText(json);
                    } catch (IOException e) {
                        log.error("Caught exception while sending message to Session Id: " + session.getId(), e.getMessage(), e);
                    }
                }
            }
        }
    }
}