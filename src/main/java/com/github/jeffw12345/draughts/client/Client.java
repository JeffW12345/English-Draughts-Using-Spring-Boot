package com.github.jeffw12345.draughts.client;

import com.github.jeffw12345.draughts.client.controller.ClientController;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Client {

    private final String CLIENT_ID = String.valueOf(UUID.randomUUID());

    public void run() {
        ClientController clientController = new ClientController(this);
    }
}

