package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;

public class DrawController {
    private final MasterClientController masterController;
    public DraughtsBoardGui view;
    private boolean drawOfferSentPending;

    public DrawController(MasterClientController masterController) {
        this.masterController = masterController;
        this.view = masterController.getView();
    }

    public void drawOfferAcceptedViewUpdate() {
        masterController.getGuiMessageController().drawOfferAcceptedMessage();
        masterController.gameOverActions();
    }

    public void drawOfferMadeByOtherClientViewUpdate() {
        masterController.getGuiMessageController().drawOfferMadeByOtherClientMessage();
        view.getOfferDrawButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(true);
    }

    public void offerDrawButtonPressedActions() {
        view.getOfferDrawButton().setEnabled(false);
        drawOfferSentPending = true;
        masterController.getGuiMessageController().youHaveOfferedDrawMessage();
        masterController.getClient().getClientOutboundMessagingService().sendDrawOfferProposal
                (masterController.getClient().getClientId());
    }

    public void acceptDrawButtonPressed() {
        masterController.gameOverActions();
        masterController.getGuiMessageController().acceptDrawButtonPressedMessage();
        masterController.getClient().getClientOutboundMessagingService().sendDrawOfferAcceptance
                (masterController.getClient().getClientId());
    }

    public void withDrawOfferIfPending() {
        if(drawOfferSentPending){
            drawOfferSentPending = false;
            masterController.getGuiMessageController().drawOfferExpiresMessage();
            view.getOfferDrawButton().setEnabled(true);
            view.getAcceptDrawButton().setEnabled(false);
        }
    }
}