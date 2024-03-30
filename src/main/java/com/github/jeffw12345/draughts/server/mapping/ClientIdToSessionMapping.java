package com.github.jeffw12345.draughts.server.mapping;

import jakarta.websocket.Session;

import java.util.concurrent.ConcurrentHashMap;

public class ClientIdToSessionMapping {
    private static final ConcurrentHashMap<String, Session> clientIdToSessionObject = new ConcurrentHashMap<>();

    public static void add(String clientId, Session session){
        clientIdToSessionObject.put(clientId, session);
    }

    public static Session getSessionFromClientId(String clientId){
        return clientIdToSessionObject.get(clientId);
    }

}
