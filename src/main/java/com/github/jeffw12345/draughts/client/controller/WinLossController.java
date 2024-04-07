package com.github.jeffw12345.draughts.client.controller;

public class WinLossController {
    private final MasterClientController masterController;


    public WinLossController(MasterClientController masterController) {
        this.masterController = masterController;
    }

    public void otherPlayerResignationActions() {
        masterController.getGuiMessageController().ifOtherPlayerResignsMessage();
        masterController.gameOverActions();
    }

    public void viewUpdateIfRedLost() {
        masterController.getGuiMessageController().ifRedLostMessage();
        masterController.gameOverActions();
    }

    public void viewUpdateIfWhiteLost() {
        masterController.getGuiMessageController().ifWhiteLostMessage();
        masterController.gameOverActions();
    }

    public void resignButtonPressed() {
        masterController.getGuiMessageController().resignButtonPressedMessage();
        masterController.gameOverActions();
        masterController.getClient()
                .getClientOutboundMessagingService().sendResignation(masterController.getClient().getClientId());
    }
}
