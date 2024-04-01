package com.github.jeffw12345.draughts.server.messaging.sending;

import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServerMessagingOutboundService {

    public static synchronized void sendJsonMessage(String json, String... clientIds){
        List<Session> sessionsList = new ArrayList<>();
        for(String clientId : clientIds){
            sessionsList.add(ClientIdToSessionMapping.getSessionFromClientId(clientId));
        }
        for (Session session : sessionsList) {
            if (session != null && session.isOpen()) {
                synchronized (session) {
                    try {
                        session.getBasicRemote().sendText(json);
                    } catch (IOException e) {
                        log.error("Caught exception while sending message to Session Id: " + session.getId(),
                                e.getMessage(),
                                e
                        );
                    }
                }
            }
        }
    }
}
