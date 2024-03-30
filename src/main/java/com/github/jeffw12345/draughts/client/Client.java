package com.github.jeffw12345.draughts.client;

import com.github.jeffw12345.draughts.client.controller.ClientController;

import com.github.jeffw12345.draughts.client.service.ClientMessagingService;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Client {
    private final String CLIENT_ID = String.valueOf(UUID.randomUUID());
    private final ClientMessagingService clientMessagingService = new ClientMessagingService(this);
    private final ClientController clientController = new ClientController(this);
    public void setUp() {
        clientMessagingService.establishConnection();
        clientController.setUp();
    }
}