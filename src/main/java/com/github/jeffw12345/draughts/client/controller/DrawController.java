package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.service.ClientMessageDispatchService;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;
import lombok.Setter;

public class DrawController {
    private final MasterClientController masterController;
    private final GuiMessageController guiMessageController;
    public DraughtsBoardGui view;
    private final ClientMessageDispatchService clientMessageDispatchService;
    private final String clientId;
    private boolean drawOfferSentPending;

    public DrawController(MasterClientController masterController) {
        this.masterController = masterController;
        this.guiMessageController = masterController.getGuiMessageController();
        this.view = masterController.getView();
        this.clientMessageDispatchService = masterController.getClient().getClientMessagingService();
        this.clientId = masterController.getClient().getClientId();
    }

    public void drawOfferAcceptedViewUpdate() {
        guiMessageController.drawOfferAcceptedMessage();
        masterController.gameOverActions();
    }

    public void drawOfferMadeByOtherClientViewUpdate() {
        guiMessageController.drawOfferMadeByOtherClientMessage();
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
        }
    }
}