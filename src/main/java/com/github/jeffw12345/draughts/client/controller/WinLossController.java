package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.io.ClientOutboundMessageService;

public class WinLossController {
    private final MasterClientController masterController;
    private final GuiMessageController guiMessageController;
    private final ClientOutboundMessageService clientMessageDispatchService;
    private final String clientId;

    public WinLossController(MasterClientController masterController) {
        this.masterController = masterController;
        this.guiMessageController = masterController.getGuiMessageController();
        this.clientMessageDispatchService = masterController.getClient().getClientOutboundMessagingService();
        this.clientId = masterController.getClient().getClientId();
    }

    public void otherPlayerResignationActions() {
        guiMessageController.ifOtherPlayerResignsMessage();
        masterController.gameOverActions();
    }

    public void viewUpdateIfRedLost() {
        guiMessageController.ifRedLostMessage();
        masterController.gameOverActions();
    }

    public void viewUpdateIfWhiteLost() {
        guiMessageController.ifWhiteLostMessage();
        masterController.gameOverActions();
    }

    public void resignButtonPressed() {
        guiMessageController.resignButtonPressedMessage();
        masterController.gameOverActions();
        clientMessageDispatchService.sendResignation(clientId);
    }
}
