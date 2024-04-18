package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;

public class BoardUpdateController {
    private final MasterClientController masterController;
    public BoardUpdateController(MasterClientController masterController) {
        this.masterController = masterController;
    }
    public void updateBoardChangeOfTurn(ServerMessageToClient messageObject) {
        masterController.getView().repaintBoard(messageObject.getBoard());
        masterController.changeTurns();
    }
    public void updateBoardSameTurn(ServerMessageToClient message) {
        masterController.getView().repaintBoard(message.getBoard());
        masterController.getGuiMessageController().turnOngoingMessage();
    }

    public void repaintBoardAtStartOfNewGame(Board board){
        masterController.getView().repaintBoard(board);
    }
}
