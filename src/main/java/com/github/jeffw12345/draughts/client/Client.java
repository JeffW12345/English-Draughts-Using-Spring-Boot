package com.github.jeffw12345.draughts.client;

import com.github.jeffw12345.draughts.client.controller.MasterClientController;

import com.github.jeffw12345.draughts.client.service.ClientMessageDispatchService;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Client {
    private final String clientId = String.valueOf(UUID.randomUUID());
    private final ClientMessageDispatchService clientMessagingService = new ClientMessageDispatchService(this);
    private final MasterClientController clientController = new MasterClientController(this);
    public void setUp() {
        clientMessagingService.establishSession();
        clientController.setUp();
    }
}