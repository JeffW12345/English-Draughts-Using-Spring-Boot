package com.github.jeffw12345.draughts.client;

import com.github.jeffw12345.draughts.client.controller.MasterClientController;

import com.github.jeffw12345.draughts.client.io.ClientInboundMessageService;
import com.github.jeffw12345.draughts.client.io.ClientOutboundMessageService;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Client {
    private final String clientId = String.valueOf(UUID.randomUUID());
    private final ClientInboundMessageService clientInboundMessagingService = new ClientInboundMessageService(this);
    private final ClientOutboundMessageService clientOutboundMessagingService = new ClientOutboundMessageService(this);
    private final MasterClientController clientController = new MasterClientController(this);
    public void setUp() {
        clientOutboundMessagingService.establishSession();
        clientController.setUp();
    }
}