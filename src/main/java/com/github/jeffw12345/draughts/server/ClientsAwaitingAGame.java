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
    public static void pop() {
        synchronized (lock) {
            clientIdsSeekingGame.pop();
        }
    }
    public boolean moreThanOnePlayerSeekingGame(){
        synchronized (lock) {
            return clientIdsSeekingGame.size() > 1;
        }
    }
}
