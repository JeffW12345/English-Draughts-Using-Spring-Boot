package com.github.jeffw12345.draughts.client.controller;

public class DrawController {
    public boolean drawOfferSentPending;
    private MasterClientController masterController;
    public DrawController(MasterClientController masterController) {
        this.masterController = masterController;
    }

    public void drawOfferAcceptedViewUpdate() {
        // Implementation
    }

    public void ifDrawOfferMadeByOtherClient() {
        // Implementation
    }

    public void drawOfferExpiryActions() {
        // Implementation
    }

    public void stalemateActions() {
    }
}