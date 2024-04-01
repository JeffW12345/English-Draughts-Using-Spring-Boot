package com.github.jeffw12345.draughts.server.mapping;

import jakarta.websocket.Session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionIdToSessionMapping {
    private static final ConcurrentHashMap<String, Session> SESSION_ID_TO_SESSION = new ConcurrentHashMap<>();

    public static void add(Session session){
        SESSION_ID_TO_SESSION.put(session.getId(), session);
    }

    public static int getSize(){
        return SESSION_ID_TO_SESSION.size();
    }

    public static Session getSessionForSessionId(String sessionId){
        return SESSION_ID_TO_SESSION.get(sessionId);
    }

    public static void remove(String sessionId) {
        SESSION_ID_TO_SESSION.remove(sessionId);
    }
}
