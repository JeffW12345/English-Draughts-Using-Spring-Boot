package com.github.jeffw12345.draughts.server;

import java.util.LinkedList;

public class ClientsAwaitingAGame {
    private static final LinkedList<String> clientIdsSeekingGame = new LinkedList<>();
    private static final Object lock = new Object();

    public static void addClientId(String clientId) {
        synchronized (lock) {
            clientIdsSeekingGame.add(clientId);
        }
    }
    public static String pop() {
        synchronized (lock) {
            return clientIdsSeekingGame.pop();
        }
    }
    public static boolean atLeastOnePlayerSeekingGame(){
        synchronized (lock) {
            return clientIdsSeekingGame.size() > 0;
        }
    }
}
