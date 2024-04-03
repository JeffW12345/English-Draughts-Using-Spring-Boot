package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;

public class BoardUpdateController {
    private MasterClientController masterController;
    public BoardUpdateController(MasterClientController controller) {
        this.masterController = controller;
    }
    public void updateBoardChangeOfTurn(ServerMessageToClient message) {
        masterController.getView().repaintBoard(message.getBoard());
        masterController.changeTurns();
        masterController.getGuiMessageController().turnOverMessage();
    }
    public void updateBoardSameTurn(ServerMessageToClient message) {
        masterController.getView().repaintBoard(message.getBoard());
        masterController.getGuiMessageController().turnOngoingMessage();

    }
}
