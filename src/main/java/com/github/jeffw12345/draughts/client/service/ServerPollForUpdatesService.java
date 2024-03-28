package com.github.jeffw12345.draughts.client.service;

import com.github.jeffw12345.draughts.client.controller.ClientController;
import com.github.jeffw12345.draughts.models.request.ClientRequestToServer;

import static com.github.jeffw12345.draughts.models.request.ClientToServerRequestType.CHECK_FOR_UPDATE;

public class ServerPollForUpdatesService {

    private final ClientController controller;

    public ServerPollForUpdatesService(ClientController controller) {
        this.controller = controller;
    }

    public void checkServerForUpdates() throws InterruptedException {
        while(true) {
            ClientRequestToServer requestForUpdate = ClientRequestToServer.builder()
                    .client(this.controller.getClient())
                    .requestType(CHECK_FOR_UPDATE)
                    .build();
            controller.getServiceUpdateProcessingService()
                    .processMessage(requestForUpdate.makeServerRequestAndGetResponse());

            Thread.sleep(200);
        }
    }
}
