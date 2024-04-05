package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;

public class BoardUpdateController {
    private final MasterClientController masterController;
    public BoardUpdateController(MasterClientController masterController) {
        this.masterController = masterController;
    }
    public void updateBoardChangeOfTurn(ServerMessageToClient message) {
        masterController.getView().repaintBoard(message.getBoard());
        masterController.changeTurns();
    }
    public void updateBoardSameTurn(ServerMessageToClient message) {
        masterController.getView().repaintBoard(message.getBoard());
        masterController.getGuiMessageController().turnOngoingMessage();
    }
}
