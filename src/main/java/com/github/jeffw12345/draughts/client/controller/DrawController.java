package com.github.jeffw12345.draughts.client.controller;

public class DrawController {
    public boolean drawOfferSentPending;
    private MasterClientController masterController;
    public DrawController(MasterClientController masterController) {
        this.masterController = masterController;
    }

    public void drawOfferAcceptedViewUpdate() {
        masterController.getGuiMessageController().ifDrawOfferAcceptedMessage();
        // TODO - Set panels blank
    }

    public void ifDrawOfferMadeByOtherClient() {
        // Implementation
    }
}