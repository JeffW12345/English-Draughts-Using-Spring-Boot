package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.service.ClientOutboundMessageService;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;

public class DrawController {
    private final MasterClientController masterController;
    private final GuiMessageController guiMessageController;
    public DraughtsBoardGui view;
    private final ClientOutboundMessageService clientMessageDispatchService;
    private final String clientId;
    private boolean drawOfferSentPending;

    public DrawController(MasterClientController masterController) {
        this.masterController = masterController;
        this.guiMessageController = masterController.getGuiMessageController();
        this.view = masterController.getView();
        this.clientMessageDispatchService = masterController.getClient().getClientOutboundMessagingService();
        this.clientId = masterController.getClient().getClientId();
    }

    public void drawOfferAcceptedViewUpdate() {
        guiMessageController.drawOfferAcceptedMessage();
        masterController.gameOverActions();
    }

    public void drawOfferMadeByOtherClientViewUpdate() {
        guiMessageController.drawOfferMadeByOtherClientMessage();
        view.getOfferDrawButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(true);
    }

    public void offerDrawButtonPressedActions() {
        view.getOfferDrawButton().setEnabled(false);
        drawOfferSentPending = true;
        guiMessageController.youHaveOfferedDrawMessage();
        clientMessageDispatchService.sendDrawOfferProposal(clientId);
    }

    public void acceptDrawButtonPressed() {
        masterController.gameOverActions();
        guiMessageController.acceptDrawButtonPressedMessage();
        clientMessageDispatchService.sendDrawOfferAcceptance(clientId);
    }

    public void withDrawOfferIfPending() {
        if(drawOfferSentPending){
            drawOfferSentPending = false;
            guiMessageController.drawOfferExpiresMessage();
            view.getOfferDrawButton().setEnabled(true);
            view.getAcceptDrawButton().setEnabled(false);
        }
    }
}