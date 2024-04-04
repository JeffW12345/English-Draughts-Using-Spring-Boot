package com.github.jeffw12345.draughts.client.io;

import jakarta.websocket.Session;
public class ClientSessionStorage {
    private static Session session;

    public static void storeSession(Session sessionToStore){
        session = sessionToStore;
    }
    public static Session retrieveSession(){
        return session;
    }
}
