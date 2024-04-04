package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.service.ClientMessageDispatchService;
import com.github.jeffw12345.draughts.models.game.Colour;

public class WinLossController {
    private final MasterClientController controller;
    private final GuiMessageController guiMessageController;
    private final ClientMessageDispatchService clientMessageDispatchService;
    private final String clientId;

    public WinLossController(MasterClientController controller) {
        this.controller = controller;
        this.guiMessageController = controller.getGuiMessageController();
        this.clientMessageDispatchService = controller.getClient().getClientMessagingService();
        this.clientId = controller.getClient().getClientId();
    }

    public void otherPlayerResignationActions() {
        guiMessageController.ifOtherPlayerResignsMessage();
        controller.gameOverActions();
    }

    public void viewUpdateIfRedLost() {
        guiMessageController.ifRedLostMessage();
        controller.gameOverActions();
    }

    public void viewUpdateIfWhiteLost() {
        guiMessageController.ifWhiteLostMessage();
        controller.gameOverActions();
    }

    public void resignButtonPressed() {
        guiMessageController.resignButtonPressedMessage();
        controller.gameOverActions();

        clientMessageDispatchService.sendResignation(clientId);
    }
}
