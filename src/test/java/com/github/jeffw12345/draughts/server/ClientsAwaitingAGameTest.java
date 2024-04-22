package com.github.jeffw12345.draughts.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

public class ClientsAwaitingAGameTest {

    @AfterEach
    void tearDown() {
        ClientsAwaitingAGame.clearList();
    }

    @Test
    void testAddClientId() {
        ClientsAwaitingAGame.addClientId("client1");
        assertTrue(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());
    }

    @Test
    void testAddClientIdMultiple() {
        ClientsAwaitingAGame.addClientId("client1");
        ClientsAwaitingAGame.addClientId("client2");
        assertTrue(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());
    }

    @Test
    void testPop() {
        ClientsAwaitingAGame.addClientId("client1");
        String poppedClientId = ClientsAwaitingAGame.pop();
        assertEquals("client1", poppedClientId);
        assertFalse(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());
    }

    @Test
    void testPopEmptyList() {
        assertThrows(NoSuchElementException.class, ClientsAwaitingAGame::pop);
    }

    @Test
    void testAtLeastOnePlayerSeekingGameTrue() {
        ClientsAwaitingAGame.addClientId("client1");
        assertTrue(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());
    }

    @Test
    void testAtLeastOnePlayerSeekingGameFalse() {
        assertFalse(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());
    }

    @Test
    void testClearList() {
        ClientsAwaitingAGame.addClientId("client1");
        ClientsAwaitingAGame.addClientId("client2");
        ClientsAwaitingAGame.addClientId("client3");

        assertTrue(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());

        ClientsAwaitingAGame.clearList();

        assertFalse(ClientsAwaitingAGame.atLeastOnePlayerSeekingGame());
    }
}
