package com.github.jeffw12345.draughts.client;

import com.github.jeffw12345.draughts.client.controller.MasterClientController;

import com.github.jeffw12345.draughts.client.service.ClientMessagingService;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Client {
    private final String clientId = String.valueOf(UUID.randomUUID());
    private final ClientMessagingService clientMessagingService = new ClientMessagingService(this);
    private final MasterClientController clientController = new MasterClientController(this);
    public void setUp() {
        clientMessagingService.establishSession();
        clientController.setUp();
    }
}