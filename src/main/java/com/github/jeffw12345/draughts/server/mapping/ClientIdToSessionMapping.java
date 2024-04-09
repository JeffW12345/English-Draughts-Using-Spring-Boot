package com.github.jeffw12345.draughts.server.mapping;

import jakarta.websocket.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientIdToSessionMapping {
    private static final ConcurrentHashMap<String, Session> clientIdToSessionObject = new ConcurrentHashMap<>();

    public static void addMapping(String clientId, Session session){
        if (clientId == null){
            return;
        }
        clientIdToSessionObject.put(clientId, session);
    }

    public static void removeEntryFromMap(String clientId){
        clientIdToSessionObject.remove(clientId);
    }
    public static Session getSessionFromClientId(String clientId){
        return clientIdToSessionObject.get(clientId);
    }

    public static String getClientIdForSession(Session session){
        return clientIdToSessionObject.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }

}
